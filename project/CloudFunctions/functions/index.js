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
