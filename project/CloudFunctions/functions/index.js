const admin = require("firebase-admin");
const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { defineSecret } = require("firebase-functions/params");
const logger = require("firebase-functions/logger");
const crypto = require("node:crypto");
const nodemailer = require("nodemailer");

admin.initializeApp();

const db = admin.firestore();
const gmailUser = defineSecret("GMAIL_USER");
const gmailAppPassword = defineSecret("GMAIL_APP_PASSWORD");
const { randomUUID } = require("node:crypto");
const FASHN_API_BASE_URL = "https://api.fashn.ai/v1";
const TRY_ON_POLL_INTERVAL_MS = 3000;
const TRY_ON_MAX_WAIT_MS = 180000;
const MAX_TRY_ON_OUTPUT_BYTES = 30 * 1024 * 1024;

// Envía un código únicamente a cuentas de Auth con un perfil activo en Firestore.
exports.requestPasswordResetCode = onCall(
  { secrets: [gmailUser, gmailAppPassword], maxInstances: 2 },
  async (request) => {
    const email = typeof request.data?.email === "string"
      ? request.data.email.trim().toLowerCase()
      : "";
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      throw new HttpsError("invalid-argument", "Correo inválido.");
    }

    let user;
    try {
      user = await admin.auth().getUserByEmail(email);
    } catch (error) {
      if (error.code === "auth/user-not-found") return { ok: true };
      throw error;
    }

    const userRef = db.collection("users").doc(user.uid);
    const profile = await userRef.get();
    // Mantiene una respuesta uniforme para no revelar correos registrados.
    if (!profile.exists || profile.data()?.eliminated === true) return { ok: true };

    const resetRef = db.collection("passwordResets").doc(user.uid);
    const now = Date.now();
    const code = String(crypto.randomInt(0, 1_000_000)).padStart(6, "0");
    const salt = crypto.randomBytes(16).toString("hex");
    const codeHash = crypto.createHash("sha256").update(`${salt}:${code}`).digest("hex");
    await db.runTransaction(async (transaction) => {
      const previous = await transaction.get(resetRef);
      const lastSentAt = previous.exists ? previous.data().lastSentAt?.toMillis?.() : null;
      if (lastSentAt && now - lastSentAt < 60_000) {
        throw new HttpsError("resource-exhausted", "Espera antes de solicitar otro código.");
      }
      transaction.set(resetRef, {
        salt,
        codeHash,
        expiresAt: admin.firestore.Timestamp.fromMillis(now + 10 * 60_000),
        attempts: 0,
        lastSentAt: admin.firestore.Timestamp.fromMillis(now),
      });
    });

    const transporter = nodemailer.createTransport({
      host: "smtp.gmail.com",
      port: 465,
      secure: true,
      auth: { user: gmailUser.value(), pass: gmailAppPassword.value() },
    });
    await transporter.sendMail({
      from: gmailUser.value(),
      to: email,
      subject: "Tu código para restablecer la contraseña de Kloth",
      text: `Tu código es ${code}. Vence en 10 minutos. Si no solicitaste este código, ignora este correo.`,
    });
    return { ok: true };
  }
);

// Comprueba el código y actualiza la contraseña sin exponer el estado de la solicitud.
exports.resetPasswordWithCode = onCall({ maxInstances: 2 }, async (request) => {
  const email = typeof request.data?.email === "string"
    ? request.data.email.trim().toLowerCase()
    : "";
  const code = typeof request.data?.code === "string" ? request.data.code.trim() : "";
  const newPassword = typeof request.data?.newPassword === "string"
    ? request.data.newPassword
    : "";
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
      || !/^\d{6}$/.test(code)
      || newPassword.length < 6) {
    throw new HttpsError("invalid-argument", "Los datos ingresados no son válidos.");
  }

  let user;
  try {
    user = await admin.auth().getUserByEmail(email);
  } catch (error) {
    if (error.code === "auth/user-not-found") {
      throw new HttpsError("not-found", "El código no es válido o venció.");
    }
    throw error;
  }

  const resetRef = db.collection("passwordResets").doc(user.uid);
  const now = Date.now();
  const outcome = await db.runTransaction(async (transaction) => {
    const snapshot = await transaction.get(resetRef);
    if (!snapshot.exists) return "invalid";

    const reset = snapshot.data();
    const expiresAt = reset.expiresAt?.toMillis?.() ?? 0;
    if (reset.consumed || expiresAt <= now || reset.attempts >= 5) {
      transaction.delete(resetRef);
      return "invalid";
    }

    const expected = Buffer.from(reset.codeHash || "", "hex");
    const actual = Buffer.from(
      crypto.createHash("sha256").update(`${reset.salt}:${code}`).digest("hex"),
      "hex"
    );
    if (expected.length !== actual.length || !crypto.timingSafeEqual(expected, actual)) {
      const attempts = reset.attempts + 1;
      if (attempts >= 5) transaction.delete(resetRef);
      else transaction.update(resetRef, { attempts });
      return "invalid";
    }

    transaction.update(resetRef, { consumed: true });
    return "valid";
  });

  if (outcome !== "valid") {
    throw new HttpsError("not-found", "El código no es válido o venció.");
  }

  await admin.auth().updateUser(user.uid, { password: newPassword });
  const batch = db.batch();
  batch.update(db.collection("users").doc(user.uid), {
    password: newPassword,
    updatedAt: new Date().toISOString(),
  });
  batch.delete(resetRef);
  await batch.commit();
  return { ok: true };
});

// Inicializa Firebase Admin para que las funciones puedan consultar y escribir en Firestore.

// Centraliza la persistencia de los eventos que aparecen en la pantalla de notificaciones.
// Usa el ID del evento como ID del documento para evitar duplicados si Firebase reintenta la funcion.
async function saveUserNotification({ event, recipientId, actorId, type, reviewId = null, thumbnailUrl = null, actorName = null, actorImageUrl = null }) {
  // Ignora eventos incompletos y acciones propias para no notificar al mismo usuario.
  if (!recipientId || !actorId || recipientId === actorId) return null;

  // Consulta el perfil del actor para completar su nombre y su imagen si el evento no los incluye.
  const actorSnapshot = await db.collection("users").doc(actorId).get();
  const actor = actorSnapshot.exists ? actorSnapshot.data() : {};

  // Todas las notificaciones se guardan bajo el perfil de su destinatario.
  const notificationRef = db.collection("users").doc(recipientId)
    .collection("notifications").doc(event.id);

  try {
    // La hora del servidor permite ordenar los eventos y mostrar su antiguedad correctamente.
    await notificationRef.create({
      actorId,
      actorName: actorName || actor.username || actor.email?.split("@")[0] || "Usuario",
      actorImageUrl: actorImageUrl || actor.imageURL || null,
      type,
      reviewId,
      thumbnailUrl,
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
      isRead: false,
    });
  } catch (error) {
    // Si el documento ya existe, este evento ya fue procesado y el reintento no debe duplicarlo.
    if (error.code !== 6 && error.code !== "already-exists") throw error;
    logger.debug("NotificaciÃ³n ya guardada para este evento", { notificationId: event.id });
  }

  // Devuelve el nombre que tambien se utiliza en el texto del push.
  return { actorName: actorName || actor.username || actor.email?.split("@")[0] || "Usuario" };
}

// Crea una notificacion persistente y un push cuando se agrega un like a una reseña.
exports.sendReviewLikeNotification = onDocumentCreated(
  "reviews/{reviewId}/likes/{likerId}",
  async (event) => {
    const { reviewId, likerId } = event.params;
    // La reseña determina quién recibira la notificacion.
    const reviewSnapshot = await db.collection("reviews").doc(reviewId).get();
    if (!reviewSnapshot.exists) return;

    const review = reviewSnapshot.data();
    const recipientId = review.userId;
    if (!recipientId || recipientId === likerId) return;

    // Busca la imagen del articulo para mostrarla como miniatura en la lista de la app.
    const articleSnapshot = review.articleId
      ? await db.collection("articles").doc(review.articleId).get()
      : null;
    const article = articleSnapshot?.exists ? articleSnapshot.data() : {};
    // Se guarda primero: no tener token FCM o fallar el push no pierde la notificacion de la app.
    const notification = await saveUserNotification({
      event,
      recipientId,
      actorId: likerId,
      type: "LIKE",
      reviewId,
      thumbnailUrl: article.imageArticle || null,
    });

    if (!notification) return;

    // El token puede faltar si el usuario no tiene un dispositivo registrado para FCM.
    const recipientSnapshot = await db.collection("users").doc(recipientId).get();
    const recipient = recipientSnapshot.exists ? recipientSnapshot.data() : {};
    const token = recipient.fcmToken || recipient.FCMToken;
    if (!token) return;

    try {
      // El push se envia despues de guardar el registro para que ambos canales compartan el evento.
      await admin.messaging().send({
        token,
        notification: {
          title: "Nuevo me gusta",
          body: `${notification.actorName} indicÃ³ que le gusta tu reseÃ±a.`,
        },
        data: { type: "LIKE", reviewId },
      });
    } catch (error) {
      // Un error de FCM no elimina la notificacion persistida en Firestore.
      logger.error("Error enviando push de like", { recipientId, reviewId, error });
    }
  }
);

// El comentario notifica al autor de la resena, excepto si comento en su propia resena.
exports.saveReviewCommentNotification = onDocumentCreated(
  "reviews/{reviewId}/comments/{commentId}",
  async (event) => {
    const { reviewId } = event.params;
    // El documento creado contiene el autor del comentario y el ID de su reseña.
    const comment = event.data.data();
    const reviewSnapshot = await db.collection("reviews").doc(reviewId).get();
    if (!reviewSnapshot.exists) return;

    const review = reviewSnapshot.data();
    // La miniatura corresponde al articulo asociado con la reseña comentada.
    const articleSnapshot = review.articleId
      ? await db.collection("articles").doc(review.articleId).get()
      : null;
    const article = articleSnapshot?.exists ? articleSnapshot.data() : {};
    await saveUserNotification({
      event,
      recipientId: review.userId,
      actorId: comment.userId,
      actorName: comment.username,
      actorImageUrl: comment.profileImageUrl,
      type: "COMENTARIO",
      reviewId,
      thumbnailUrl: article.imageArticle || null,
    });
  }
);

// Una resena nueva notifica a quien publico el articulo reseñado.
exports.saveNewReviewNotification = onDocumentCreated(
  "reviews/{reviewId}",
  async (event) => {
    const { reviewId } = event.params;
    // La reseña indica quién la escribió y a qué artículo pertenece.
    const review = event.data.data();
    if (!review.articleId) return;

    const articleSnapshot = await db.collection("articles").doc(review.articleId).get();
    if (!articleSnapshot.exists) return;
    const article = articleSnapshot.data();
    // El creador del artículo recibe la notificacion de la reseña nueva.
    await saveUserNotification({
      event,
      recipientId: article.idUser,
      actorId: review.userId,
      type: "NUEVA_RESENA",
      reviewId,
      thumbnailUrl: article.imageArticle || null,
    });
  }
);

// La relacion de seguidores es la fuente de verdad para notificar un nuevo seguimiento.
exports.saveNewFollowerNotification = onDocumentCreated(
  "users/{recipientId}/followers/{actorId}",
  async (event) => {
    const { recipientId, actorId } = event.params;
    // El ID del documento follower identifica al usuario que comenzó a seguir al destinatario.
    await saveUserNotification({
      event,
      recipientId,
      actorId,
      type: "NUEVO_SEGUIDOR",
    });
  }
);

// Genera automáticamente un resumen de IA usando OpenRouter cuando se crea una nueva reseña
exports.generateArticleAiSummary = onDocumentCreated(
  "reviews/{reviewId}",
  async (event) => {
    const review = event.data.data();
    const articleId = review.articleId;
    if (!articleId) return;

    try {
      const reviewsSnapshot = await db.collection("reviews")
        .where("articleId", "==", articleId)
        .get();

      const reviews = reviewsSnapshot.docs
        .map((doc) => doc.data())
        .filter((r) => !r.eliminated);

      if (reviews.length === 0) return;

      const reviewsText = reviews.map((r) => `- ${r.text || ""}`).join("\n");
      const apiKey = process.env.OPENROUTER_API_KEY || "sk-or-v1-API_KEY";
      const prompt = `Actúa como un experto en análisis de calidad de ropa en un e-commerce. Lee las siguientes opiniones de usuarios sobre un producto y genera un resumen conciso, amigable y directo en español (máximo 3 oraciones) destacando los pros, contras y la opinión general:\n${reviewsText}`;

      const response = await fetch("https://openrouter.ai/api/v1/chat/completions", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${apiKey}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          model: "nvidia/nemotron-3-ultra-550b-a55b:free",
          messages: [{ role: "user", content: prompt }],
        }),
      });

      if (!response.ok) {
        logger.error("Error en OpenRouter API", { status: response.status });
        return;
      }

      const data = await response.json();
      const summary = data.choices?.[0]?.message?.content?.trim();

      if (!summary) return;

      await db.collection("articles").doc(articleId).set({
        aiSummary: summary,
        summaryReviewCount: reviews.length,
      }, { merge: true });

      logger.info("Resumen de IA generado exitosamente para el artículo", { articleId, reviewsCount: reviews.length });
    } catch (error) {
      logger.error("Error generando resumen de IA para artículo", { articleId, error });
    }
  }
);
// Try On se inicia directamente desde la app. La función espera la predicción de FASHN,
// persiste la imagen generada y devuelve el resultado en una sola respuesta callable.
exports.generateTryOn = onCall(
  {
    timeoutSeconds: 300,
    memory: "1GiB",
    // Cloud Run debe aceptar la llamada HTTP; Firebase Auth se valida en el cuerpo callable.
    invoker: "public",
  },
  async (request) => {
    logger.info("generateTryOn recibió una llamada", {
      hasAuthContext: Boolean(request.auth?.uid),
      hasAppCheckContext: Boolean(request.app),
    });
    const userId = request.auth?.uid;
    if (!userId) {
      logger.warn("generateTryOn recibió una llamada sin contexto de Firebase Auth", {
        hasAppCheckContext: Boolean(request.app),
      });
      throw new HttpsError("unauthenticated", "Debes iniciar sesión para usar Try On.");
    }

    const { publicationId, baseModelPicId } = request.data || {};
    if (typeof publicationId !== "string" || !publicationId.trim() ||
        typeof baseModelPicId !== "string" || !baseModelPicId.trim()) {
      throw new HttpsError("invalid-argument", "Falta la publicación o la foto base.");
    }

    const baseModelPicRef = db.collection("users").doc(userId)
      .collection("modelPics").doc(baseModelPicId);
    const publicationRef = db.collection("articles").doc(publicationId);

    const [baseModelPicSnapshot, publicationSnapshot] = await Promise.all([
      baseModelPicRef.get(),
      publicationRef.get(),
    ]);

    if (!baseModelPicSnapshot.exists) {
      throw new HttpsError("not-found", "No se encontró la foto base seleccionada.");
    }
    if (!publicationSnapshot.exists) {
      throw new HttpsError("not-found", "No se encontró la publicación.");
    }    const baseModelPic = baseModelPicSnapshot.data();
    const publication = publicationSnapshot.data();
    const articleType = String(publication.typeArticle || "").trim().toLowerCase();
    const expectedBasePathPrefix = `modelPics/${userId}/`;
    const baseStoragePath = baseModelPic.storagePath;
    const baseImageFileName = typeof baseStoragePath === "string"
      ? baseStoragePath.slice(expectedBasePathPrefix.length)
      : "";

    if (baseModelPic.type !== "base" ||
        typeof baseStoragePath !== "string" ||
        !baseStoragePath.startsWith(expectedBasePathPrefix) ||
        !baseImageFileName || baseImageFileName.includes("/")) {
      throw new HttpsError("permission-denied", "La foto seleccionada no es una foto base tuya.");
    }
    if (!new Set(["prenda", "outfit"]).has(articleType) || publication.eliminated === true) {
      throw new HttpsError("failed-precondition", "Try On solo está disponible para prendas y outfits activos.");
    }

    const modelImageUrl = await createFirebaseDownloadUrl(baseStoragePath);
    const productImageUrl = requireHttpsImageUrl(publication.imageArticle, "imagen de la publicación");
    const apiKey = process.env.FASHN_API_KEY;
    if (!apiKey) {
      logger.error("FASHN_API_KEY no está configurada en el entorno de Cloud Functions.");
      throw new HttpsError("internal", "El servicio Try On no está configurado.");
    }

    let predictionId;
    try {
      const prompt = articleType === "outfit"
        ? "Wear the complete outfit and all visible accessories shown in the product image, including any clothing, headwear, bags, and footwear."
        : "Wear only the single fashion item or accessory shown in the product image, replacing only the corresponding item on the model.";

      // Una sola predicción Try-On Max: fast + 1k + una salida = 1 crédito.
      const fashnRequest = {
        model_name: "tryon-max",
        inputs: {
          model_image: modelImageUrl,
          product_image: productImageUrl,
          prompt,
          generation_mode: "fast",
          resolution: "1k",
          num_images: 1,
          output_format: "jpeg",
          return_base64: false,
        },
      };

      const prediction = await fetchFashnJson(`${FASHN_API_BASE_URL}/run`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${apiKey}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(fashnRequest),
      }, "iniciar la predicción");

      predictionId = prediction.id;
      if (typeof predictionId !== "string" || !predictionId) {
        logger.error("FASHN no devolvió un ID de predicción.");
        throw new HttpsError("unavailable", "FASHN no pudo iniciar la generación.");
      }

      const outputUrl = await waitForFashnOutput(predictionId, apiKey);
      const output = await downloadFashnOutput(outputUrl);
      return await persistTryOnResult({
        userId,
        publicationId,
        baseModelPicId,
        publicationTitle: publication.title,
        output,
      });
    } catch (error) {
      if (error instanceof HttpsError) throw error;
      logger.error("Error generando Try On", {
        userId,
        publicationId,
        predictionId: predictionId || null,
        error: error.message,
      });
      throw new HttpsError("internal", "No se pudo generar la imagen. Inténtalo de nuevo.");
    }
  }
);

function requireHttpsImageUrl(value, description) {
  if (typeof value !== "string" || !value.trim()) {
    throw new HttpsError("failed-precondition", `La ${description} no tiene una URL.`);
  }

  try {
    const url = new URL(value);
    if (url.protocol !== "https:") throw new Error("URL scheme must be HTTPS");
    return url.toString();
  } catch (_error) {
    throw new HttpsError("failed-precondition", `La URL de la ${description} no es válida.`);
  }
}

async function createFirebaseDownloadUrl(storagePath) {
  const bucket = admin.storage().bucket();
  const imageFile = bucket.file(storagePath);
  let metadata;
  try {
    [metadata] = await imageFile.getMetadata();
  } catch (error) {
    if (error.code === 404) {
      throw new HttpsError("not-found", "No se encontró el archivo de la foto base en Storage.");
    }
    throw error;
  }

  const token = metadata.metadata?.firebaseStorageDownloadTokens?.split(",")[0];
  if (!token) {
    throw new HttpsError("not-found", "No se encontró el archivo de la foto base en Storage.");
  }

  return `https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/${
    encodeURIComponent(storagePath)
  }?alt=media&token=${token}`;
}

async function fetchFashnJson(url, options, action) {
  let response;
  try {
    response = await fetch(url, {
      ...options,
      signal: AbortSignal.timeout(30000),
    });
  } catch (error) {
    logger.error(`No se pudo ${action} en FASHN`, { error: error.message });
    throw new HttpsError("unavailable", `No se pudo ${action} en FASHN.`);
  }

  let payload;
  try {
    payload = await response.json();
  } catch (_error) {
    logger.error(`FASHN devolvió una respuesta no JSON al ${action}`, { status: response.status });
    throw new HttpsError("unavailable", "FASHN devolvió una respuesta inválida.");
  }

  if (!response.ok) {
    logger.error(`FASHN rechazó la solicitud al ${action}`, { status: response.status });
    throw new HttpsError("unavailable", "FASHN no pudo procesar la solicitud.");
  }

  return payload;
}

async function waitForFashnOutput(predictionId, apiKey) {
  const deadline = Date.now() + TRY_ON_MAX_WAIT_MS;
  const statusUrl = `${FASHN_API_BASE_URL}/status/${encodeURIComponent(predictionId)}`;

  while (Date.now() < deadline) {
    await new Promise((resolve) => setTimeout(resolve, TRY_ON_POLL_INTERVAL_MS));
    const prediction = await fetchFashnJson(statusUrl, {
      method: "GET",
      headers: { "Authorization": `Bearer ${apiKey}` },
    }, "consultar el estado");

    if (prediction.status === "completed") {
      const outputUrl = Array.isArray(prediction.output) ? prediction.output[0] : null;
      if (typeof outputUrl !== "string" || !outputUrl) {
        logger.error("FASHN completó Try On sin devolver imagen.", { predictionId });
        throw new HttpsError("internal", "FASHN no devolvió una imagen de resultado.");
      }
      return outputUrl;
    }

    if (prediction.status === "failed") {
      logger.warn("FASHN marcó la predicción como fallida", {
        predictionId,
        errorName: prediction.error?.name || null,
      });
      throw new HttpsError("failed-precondition", "FASHN no pudo generar la imagen con estas fotos.");
    }

    if (!["starting", "in_queue", "processing"].includes(prediction.status)) {
      logger.error("FASHN devolvió un estado desconocido", {
        predictionId,
        status: prediction.status || null,
      });
      throw new HttpsError("unavailable", "FASHN devolvió un estado de generación desconocido.");
    }
  }

  logger.error("Timeout esperando la predicción FASHN", { predictionId });
  throw new HttpsError("deadline-exceeded", "La generación tardó demasiado. Inténtalo de nuevo.");
}

async function downloadFashnOutput(outputUrl) {
  let url;
  try {
    url = new URL(outputUrl);
  } catch (_error) {
    throw new HttpsError("internal", "FASHN devolvió una URL de resultado inválida.");
  }

  if (url.protocol !== "https:" || !["cdn.fashn.ai", "media.fashn.ai"].includes(url.hostname)) {
    throw new HttpsError("internal", "FASHN devolvió un dominio de resultado no permitido.");
  }

  let response;
  try {
    response = await fetch(url, { signal: AbortSignal.timeout(30000) });
  } catch (error) {
    logger.error("No se pudo descargar la imagen de FASHN", { error: error.message });
    throw new HttpsError("unavailable", "No se pudo descargar la imagen generada.");
  }

  if (!response.ok) {
    logger.error("Fallo al descargar el resultado de FASHN", { status: response.status });
    throw new HttpsError("unavailable", "No se pudo descargar la imagen generada.");
  }

  const contentType = (response.headers.get("content-type") || "image/jpeg").split(";")[0];
  if (!contentType.startsWith("image/")) {
    throw new HttpsError("internal", "FASHN devolvió un archivo que no es una imagen.");
  }

  const buffer = Buffer.from(await response.arrayBuffer());
  if (!buffer.length || buffer.length > MAX_TRY_ON_OUTPUT_BYTES) {
    throw new HttpsError("resource-exhausted", "La imagen generada tiene un tamaño no permitido.");
  }

  return { buffer, contentType };
}

async function persistTryOnResult({
  userId,
  publicationId,
  baseModelPicId,
  publicationTitle,
  output,
}) {
  const extension = output.contentType === "image/png" ? "png" : "jpg";
  const storagePath = `modelPics/${userId}/${randomUUID()}.${extension}`;
  const bucket = admin.storage().bucket();
  const imageFile = bucket.file(storagePath);
  const downloadToken = randomUUID();
  const modelPicRef = db.collection("users").doc(userId).collection("modelPics").doc();

  try {
    await imageFile.save(output.buffer, {
      resumable: false,
      metadata: {
        contentType: output.contentType,
        metadata: { firebaseStorageDownloadTokens: downloadToken },
      },
    });

    const imageUrl = `https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/${
      encodeURIComponent(storagePath)
    }?alt=media&token=${downloadToken}`;

    await modelPicRef.create({
      name: `Try On - ${String(publicationTitle || "Resultado").trim().slice(0, 80)}`,
      imageUrl,
      storagePath,
      type: "generated",
      sourcePublicationId: publicationId,
      baseModelPicId,
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
    });

    logger.info("Try On generado y guardado", {
      userId,
      publicationId,
      modelPicId: modelPicRef.id,
    });

    return { modelPicId: modelPicRef.id, imageUrl };
  } catch (error) {
    await imageFile.delete({ ignoreNotFound: true }).catch(() => {});
    throw error;
  }
}
