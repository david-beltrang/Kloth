package com.example.kloth.data.repository

import com.example.kloth.data.dataresource.CommentRemoteDataSource
import com.example.kloth.data.dataresource.ReviewRemoteDataSource
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.UpdateReviewDto
import com.example.kloth.data.dto.toComentarioData
import com.example.kloth.data.dto.toReviewData
import com.example.kloth.data.local.ReviewData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reseñas: consulta por id (con comentarios), creación, "me gusta" y
 * eliminación. `isLikedByMe` no persiste en el backend, vive en memoria del
 * ViewModel.
 */
@Singleton
class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRemoteDataSource,
    private val commentDataSource: CommentRemoteDataSource,
    private val userRepository: UserRepository
) {

    /** Reseña con su autor y sus comentarios resueltos (join cliente). */
    suspend fun getReviewWithComments(id: String): Result<ReviewData> {
        return try {
            val users = userRepository.getUsersMap()
            val dto = remoteDataSource.getReviewById(id)
            val comments = commentDataSource.getCommentsByReview(id)
                .filter { !it.eliminated }
                .map { comment -> comment.toComentarioData(users[comment.userId]) }

            Result.success(dto.toReviewData(users[dto.userId], comments))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Reseña ligera con su autor, sin comentarios (útil para prellenar edición). */
    suspend fun getReviewById(id: String): Result<ReviewData> {
        return try {
            val users = userRepository.getUsersMap()
            val dto = remoteDataSource.getReviewById(id)
            Result.success(dto.toReviewData(users[dto.userId]))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(dto: CreateReviewDto): Result<Unit> {
        return try {
            remoteDataSource.createReview(dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(id: String, dto: UpdateReviewDto): Result<Unit> {
        return try {
            remoteDataSource.updateReview(id, dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun likeReview(id: String, likesCount: Int): Result<Unit> {
        return try {
            remoteDataSource.updateReview(id, UpdateReviewDto(likesCount = likesCount))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(id: String): Result<Unit> {
        return try {
            remoteDataSource.deleteReview(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Todas las reseñas del usuario con autor y comentarios. */
    suspend fun getUserReviews(userId: String): Result<List<ReviewData>> {
        return try {
            val users = userRepository.getUsersMap()
            val reviews = remoteDataSource.getReviewsByUser(userId).filter { !it.eliminated }

            val reviewList = reviews.map { review ->
                val comments = commentDataSource.getCommentsByReview(review.reviewId.toString())
                    .filter { !it.eliminated }
                    .map { comment -> comment.toComentarioData(users[comment.userId]) }

                review.toReviewData(users[review.userId], comments)
            }
            Result.success(reviewList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
