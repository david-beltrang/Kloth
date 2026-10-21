const admin = require("firebase-admin");
const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const logger = require("firebase-functions/logger");

admin.initializeApp();

const db = admin.firestore();

exports.sendReviewLikeNotification = onDocumentCreated(
  "reviews/{reviewId}/likes/{likerId}",
  async (event) => {
    const reviewId = event.params.reviewId;
    const likerId = event.params.likerId;
    logger.log("Like de review recibido", { reviewId, likerId });

    // 1. Obtener el autor de la reseña
    const reviewDoc = await db.collection("reviews").doc(reviewId).get();
    if (!reviewDoc.exists) {
      logger.warn("No se encontró la review del like", { reviewId });
      return;
    }

    const userId = reviewDoc.data().userId;
    if (!userId || userId === likerId) {
      logger.log("Like propio o review sin autor; no se envía notificación", { reviewId, likerId });
      return;
    }

    // 2. Obtener FCM Token del destinatario
    const userDoc = await db.collection("users").doc(userId).get();
    if (!userDoc.exists) {
      logger.warn("No se encontró el usuario autor de la review", { userId, reviewId });
      return;
    }
    const userData = userDoc.data();
    const targetToken = userData.fcmToken || userData.FCMToken;

    if (!targetToken) {
      logger.warn("El autor de la review no tiene token FCM", { userId });
      return;
    }

    // 3. Obtener nombre de quien dio like
    const likerDoc = await db.collection("users").doc(likerId).get();
    const likerName = likerDoc.exists ? likerDoc.data().username : "Alguien";

    // 4. Construir payload
    const message = {
      token: targetToken,
      notification: {
        title: "Nuevo me gusta",
        body: `${likerName || "Alguien"} indicó que le gusta tu reseña.`,
      },
    };

    // 5. Enviar mediante Admin SDK
    try {
      await admin.messaging().send(message);
      logger.log(`Notificación enviada a ${userId}`);
    } catch (error) {
      logger.error("Error enviando notificación:", error);
    }
  }
);
