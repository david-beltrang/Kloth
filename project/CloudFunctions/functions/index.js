const admin = require("firebase-admin");
const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const logger = require("firebase-functions/logger");

admin.initializeApp();

const db = admin.firestore();

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
