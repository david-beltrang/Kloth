package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.services.ReviewRemoteDataSource
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewDtoGeneric
import com.example.kloth.data.dto.ReviewFirestoreDto
import com.example.kloth.data.dto.UpdateReviewDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewRemoteDataSource {

    override suspend fun getAllReviews(): List<ReviewDtoGeneric> {
        val snapshot = db.collection("reviews").get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(ReviewFirestoreDto::class.java)?.copy(reviewId = doc.id)
        }
    }

    override suspend fun getReviewById(id: String, currentUserId: String): ReviewDtoGeneric {
        val reviewRef = db.collection("reviews").document(id)

        // Obtiene la reseña
        val doc = reviewRef.get().await()
        val review = doc.toObject(ReviewFirestoreDto::class.java)?.copy(reviewId = doc.id)
            ?: throw Exception("Reseña no encontrada")

        if (currentUserId.isNotEmpty()) {
            // Verifica si el usuario ya dio like
            val likeSnapshot = reviewRef.collection("likes").document(currentUserId).get().await()
            val hasLiked = likeSnapshot.exists()

            if (hasLiked) {
                review.liked = true
            }
        }

        return review
    }

    override suspend fun createReview(dto: CreateReviewDto) {
        db.collection("reviews").add(dto).await()
    }

    override suspend fun updateReview(id: String, dto: UpdateReviewDto) {
        db.collection("reviews").document(id).set(dto, SetOptions.merge()).await()
    }

    override suspend fun deleteReview(id: String) {
        db.collection("reviews").document(id).delete().await()
    }

    override suspend fun sendOrDeleteLike(reviewId: String, userId: String) {
        val reviewRef = db.collection("reviews").document(reviewId)
        val likeRef = reviewRef.collection("likes").document(userId)
        //Lee y escribe de forma atómica → o se ejecuta todo o no se ejecuta nada.
        //Evita inconsistencias si dos usuarios modifican la misma reseña al mismo tiempo.
        db.runTransaction { transaction ->
            val likeDoc = transaction.get(likeRef)
            if (likeDoc.exists()) {
                // Quitar like
                transaction.delete(likeRef) // Elimina el documento de la subcolección
                transaction.update(reviewRef, "likesCount", FieldValue.increment(-1)) // Resta 1 al contador
            } else {
                // Dar like
                transaction.set(likeRef, mapOf("timestamp" to FieldValue.serverTimestamp())) // Crea el doc del like
                transaction.update(reviewRef, "likesCount", FieldValue.increment(1)) // Suma 1 al contador
            }
        }
    }
}