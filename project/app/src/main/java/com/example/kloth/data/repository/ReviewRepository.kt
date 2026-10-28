package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImpl
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.CreateCommentDto
import com.example.kloth.data.dto.UpdateReviewDto
import com.example.kloth.data.local.ReviewData
import com.example.kloth.data.local.ComentarioReviewData
import com.example.kloth.data.local.ReviewLikeState
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

// Centraliza las operaciones de reseñas y comentarios antes de llegar a Firestore.
@Singleton
class ReviewRepository @Inject constructor(
    private val reviewDataSource: ReviewFirestoreDataSourceImpl,
    private val userRepository: UserRepository
) {

    fun observeReviewsForArticle(articleId: String): Flow<List<ReviewData>> = flow {
        // Carga los perfiles una vez y los reutiliza al transformar cada emision de reseñas.
        val users = userRepository.getAllUsers()
        val usersById = users.associateBy { it.toUserProfileInfo().id }
        emitAll(
            reviewDataSource.observeReviewsForArticle(articleId).map { reviews ->
                reviews.filter { it.eliminated != true }.map { dto ->
                    // El conteo viene de la subcoleccion de comentarios de cada reseña.
                    val commentsCount = reviewDataSource.getCommentsCount(dto.reviewId)
                    dto.toReviewData(usersById[dto.authorIdKey()])
                        .copy(commentsCountText = commentsCount.toString())
                }
            }
        )
    }

    suspend fun getAllReviews(): Result<List<ReviewData>> = try {
        val reviews = reviewDataSource.getAllReviews()
        val users = userRepository.getAllUsers()

        val usersById = users.associateBy { it.toUserProfileInfo().id }

        val reviewsWithAuthor = reviews
            .filter { it.eliminated != true }
            .map { reviewDto ->
                reviewDto.toReviewData(usersById[reviewDto.authorIdKey()])
            }
        Result.success(reviewsWithAuthor)
    } catch (e: Exception) {
        Log.e("ReviewRepository", "Error al cargar las reseñas", e)
        Result.failure(e)
    }

    suspend fun getReviewById(id: String, currentUserId: String? = null): Result<ReviewData> = try {
        val reviewDto = reviewDataSource.getReviewById(id)
        val users = userRepository.getAllUsers()

        val usersById = users.associateBy { it.toUserProfileInfo().id }
        // Combina reseña, autor, comentarios y estado del like para la pantalla de detalle.
        val detail = reviewDto.toReviewData(usersById[reviewDto.authorIdKey()])
        val comments = reviewDataSource.getComments(id).map { comment ->
            val isLikedByMe = currentUserId?.let { userId ->
                reviewDataSource.isCommentLikedByUser(id, comment.commentId, userId)
            } ?: false
            comment.toComentarioReviewData(currentUserId, isLikedByMe)
        }
        val isLiked = currentUserId?.let { reviewDataSource.isReviewLikedByUser(id, it) } ?: false
        Result.success(detail.copy(
            comentariosList = comments,
            isLikedByMe = isLiked,
            commentsCountText = comments.size.toString()
        ))
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo cargar la reseña $id", e)
        Result.failure(e)
    }

    suspend fun createReview(
        articleId: String,
        userId: String,
        rating: Int,
        text: String
    ): Result<Unit> = try {
        val dto = CreateReviewDto(
            userId = userId,
            articleId = articleId,
            rating = rating,
            text = text,
            createdAt = Instant.now().toString()
        )
        // La Cloud Function de reseñas guarda la notificacion para quien publico el articulo.
        reviewDataSource.createReview(dto)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo crear la reseña", e)
        Result.failure(e)
    }

    suspend fun updateReview(id: String, dto: UpdateReviewDto): Result<Unit> = try {
        reviewDataSource.updateReview(id, dto)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo actualizar la reseña $id", e)
        Result.failure(e)
    }

    suspend fun deleteReview(id: String): Result<Unit> = try {
        reviewDataSource.deleteReview(id)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo eliminar la reseña $id", e)
        Result.failure(e)
    }

    suspend fun createComment(
        reviewId: String,
        userId: String,
        username: String,
        profileImageUrl: String?,
        text: String
    ): Result<ComentarioReviewData> = try {
        val dto = CreateCommentDto(
            userId = userId,
            username = username,
            profileImageUrl = profileImageUrl,
            text = text.trim(),
            createdAt = Instant.now().toString()
        )
        // La Cloud Function del comentario registra la notificacion para el autor de la reseña.
        val commentId = reviewDataSource.createComment(reviewId, dto)
        Result.success(
            ComentarioReviewData(
                id = commentId,
                authorId = userId,
                authorName = username,
                timeAgo = "Ahora",
                avatarRes = com.example.kloth.R.drawable.profile,
                avatarUrl = profileImageUrl,
                commentText = text.trim(),
                isMyComment = true
            )
        )
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo crear el comentario", e)
        Result.failure(e)
    }

    suspend fun deleteComment(reviewId: String, commentId: String): Result<Unit> = try {
        reviewDataSource.deleteComment(reviewId, commentId)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo eliminar el comentario $commentId", e)
        Result.failure(e)
    }

    suspend fun toggleReviewLike(reviewId: String, userId: String): Result<ReviewLikeState> = try {
        // Crear el documento like activa la Cloud Function que persiste la notificacion y envia el push.
        val likeState = reviewDataSource.toggleReviewLike(reviewId, userId)
        Result.success(likeState)
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo actualizar el me gusta de la reseña $reviewId", e)
        Result.failure(e)
    }

    suspend fun toggleCommentLike(
        reviewId: String,
        commentId: String,
        userId: String
    ): Result<ReviewLikeState> = try {
        Result.success(reviewDataSource.toggleCommentLike(reviewId, commentId, userId))
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo actualizar el me gusta del comentario $commentId", e)
        Result.failure(e)
    }

    suspend fun isReviewLikedByUser(reviewId: String, userId: String): Result<Boolean> = try {
        Result.success(reviewDataSource.isReviewLikedByUser(reviewId, userId))
    } catch (e: Exception) {
        Log.e("ReviewRepository", "No se pudo consultar el me gusta de la reseña $reviewId", e)
        Result.failure(e)
    }
}
