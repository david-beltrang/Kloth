package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.services.ReviewRemoteDataSource
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.CommentFirestoreDto
import com.example.kloth.data.dto.CreateCommentDto
import com.example.kloth.data.dto.ReviewDtoGeneric
import com.example.kloth.data.dto.ReviewFirestoreDto
import com.example.kloth.data.dto.UpdateReviewDto
import com.example.kloth.data.local.ReviewLikeState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewRemoteDataSource {

    fun observeReviewsForArticle(articleId: String): Flow<List<ReviewFirestoreDto>> = callbackFlow {
        val listener = db.collection("reviews").whereEqualTo("articleId", articleId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val reviews = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ReviewFirestoreDto::class.java)?.copy(reviewId = doc.id)
                    }
                    trySend(reviews)
                }
            }
        awaitClose { listener.remove() }
    }

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
        val reviewRef = db.collection("reviews").document(id)
        listOf("comments", "likes").forEach { subcollection ->
            val documents = reviewRef.collection(subcollection).get().await().documents
            documents.forEach { document -> document.reference.delete().await() }
        }
        reviewRef.delete().await()
    }

    suspend fun getComments(reviewId: String): List<CommentFirestoreDto> {
        return db.collection("reviews").document(reviewId).collection("comments")
            .get().await().documents.mapNotNull { doc ->
                doc.toObject(CommentFirestoreDto::class.java)?.copy(commentId = doc.id)
            }
    }

    suspend fun createComment(reviewId: String, dto: CreateCommentDto): String {
        val reviewRef = db.collection("reviews").document(reviewId)
        val commentRef = reviewRef.collection("comments").document()
        val currentCount = getCommentsCount(reviewId)
        db.runTransaction { transaction ->
            val review = transaction.get(reviewRef)
            transaction.set(commentRef, dto)
            val count = review.getLong("commentsCount")?.toInt() ?: currentCount
            transaction.update(reviewRef, "commentsCount", count + 1)
        }.await()
        return commentRef.id
    }

    suspend fun deleteComment(reviewId: String, commentId: String) {
        val reviewRef = db.collection("reviews").document(reviewId)
        val commentRef = reviewRef.collection("comments").document(commentId)
        val currentCount = getCommentsCount(reviewId)
        db.runTransaction { transaction ->
            val comment = transaction.get(commentRef)
            val review = transaction.get(reviewRef)
            if (comment.exists()) {
                transaction.delete(commentRef)
                val count = review.getLong("commentsCount")?.toInt() ?: currentCount
                transaction.update(reviewRef, "commentsCount", count - 1)
            }
        }.await()
    }

    suspend fun getCommentsCount(reviewId: String): Int = db.collection("reviews")
        .document(reviewId).collection("comments").get().await().size()

    suspend fun isReviewLikedByUser(reviewId: String, userId: String): Boolean {
        return db.collection("reviews").document(reviewId)
            .collection("likes").document(userId).get().await().exists()
    }

    suspend fun toggleReviewLike(reviewId: String, userId: String): ReviewLikeState {
        val reviewRef = db.collection("reviews").document(reviewId)
        val likeRef = reviewRef.collection("likes").document(userId)
        return db.runTransaction { transaction ->
            val likeSnapshot = transaction.get(likeRef)
            val reviewSnapshot = transaction.get(reviewRef)
            val currentLikes = reviewSnapshot.getLong("likesCount")?.toInt() ?: 0
            if (likeSnapshot.exists()) {
                transaction.delete(likeRef)
                transaction.update(reviewRef, "likesCount", FieldValue.increment(-1))
                ReviewLikeState(isLikedByMe = false, likesCount = currentLikes - 1)
            } else {
                transaction.set(likeRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(reviewRef, "likesCount", FieldValue.increment(1))
                ReviewLikeState(isLikedByMe = true, likesCount = currentLikes + 1)
            }
        }.await()
    }
}
