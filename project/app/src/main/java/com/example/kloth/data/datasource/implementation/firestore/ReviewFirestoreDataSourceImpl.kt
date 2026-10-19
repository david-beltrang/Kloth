package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.services.ReviewRemoteDataSource
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewDtoGeneric
import com.example.kloth.data.dto.ReviewFirestoreDto
import com.example.kloth.data.dto.UpdateReviewDto
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

    override suspend fun getReviewById(id: String): ReviewDtoGeneric {
        val doc = db.collection("reviews").document(id).get().await()
        return doc.toObject(ReviewFirestoreDto::class.java)?.copy(reviewId = doc.id)
            ?: throw Exception("Reseña no encontrada")
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
}