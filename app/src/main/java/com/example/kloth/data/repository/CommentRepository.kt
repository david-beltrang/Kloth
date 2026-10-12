package com.example.kloth.data.repository

import com.example.kloth.data.dataresource.CommentRemoteDataSource
import com.example.kloth.data.dto.CreateCommentDto
import com.example.kloth.data.dto.UpdateCommentDto
import com.example.kloth.data.dto.toComentarioData
import com.example.kloth.data.local.ComentarioReviewData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Comentarios de una reseña: creación, "me gusta" y eliminación.
 */
@Singleton
class CommentRepository @Inject constructor(
    private val remoteDataSource: CommentRemoteDataSource,
    private val userRepository: UserRepository
) {

    suspend fun getCommentsByReview(reviewId: String): Result<List<ComentarioReviewData>> {
        return try {
            val users = userRepository.getUsersMap()
            val comments = remoteDataSource.getCommentsByReview(reviewId)
                .filter { !it.eliminated }
                .map { it.toComentarioData(users[it.userId]) }
            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createComment(reviewId: String, text: String, userId: Int): Result<ComentarioReviewData> {
        return try {
            val users = userRepository.getUsersMap()
            val dto = remoteDataSource.createComment(
                reviewId = reviewId,
                dto = CreateCommentDto(userId = userId, text = text)
            )
            val comentario = dto.toComentarioData(users[dto.userId])
            Result.success(comentario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun likeComment(id: String, likesCount: Int): Result<Unit> {
        return try {
            remoteDataSource.updateComment(id, UpdateCommentDto(likesCount = likesCount))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(id: String): Result<Unit> {
        return try {
            remoteDataSource.deleteComment(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
