package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImpl
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.UpdateReviewDto
import com.example.kloth.data.local.ReviewData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.merge
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val reviewDataSource: ReviewFirestoreDataSourceImpl,
    private val userRepository: UserRepository
) {
    private val _reviewsChanged = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    /** Avisa cada vez que se crea, edita o elimina una reseña para que las pantallas se actualicen. */
    val reviewsChanged: Flow<Unit> = merge(_reviewsChanged, userRepository.usersChanged)

    suspend fun getAllReviews(): Result<List<ReviewData>> = try {
        val reviews = coroutineScope {
            val reviewsDeferred = async { runCatching { reviewDataSource.getAllReviews() }.getOrDefault(emptyList()) }
            val usersDeferred = async { runCatching { userRepository.getAllUsers() }.getOrDefault(emptyList()) }

            val usersById = usersDeferred.await().associateBy { it.toUserProfileInfo().id }

            reviewsDeferred.await()
                .filter { it.eliminated != true }
                .map { reviewDto ->
                    reviewDto.toReviewData(usersById[reviewDto.authorKey()])
                }
        }
        Result.success(reviews)
    } catch (e: Throwable) {
        Log.e(TAG, "Error al cargar las reseñas", e)
        Result.failure(e as? Exception ?: Exception(e))
    }

    suspend fun getReviewById(id: String): Result<ReviewData> = try {
        val detail = coroutineScope {
            val reviewDeferred = async { reviewDataSource.getReviewById(id) }
            val usersDeferred = async { userRepository.getAllUsers() }

            val usersById = usersDeferred.await().associateBy { it.toUserProfileInfo().id }
            val reviewDto = reviewDeferred.await()
            reviewDto.toReviewData(usersById[reviewDto.authorKey()])
        }
        Result.success(detail)
    } catch (e: HttpException) {
        Log.e(TAG, "Error del backend al cargar la reseña $id", e)
        Result.failure(e)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo conectar al cargar la reseña $id", e)
        Result.failure(e)
    }

    suspend fun createReview(dto: CreateReviewDto): Result<Unit> = try {
        reviewDataSource.createReview(dto)
        _reviewsChanged.tryEmit(Unit)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo crear la reseña", e)
        Result.failure(e)
    }

    suspend fun updateReview(id: String, dto: UpdateReviewDto): Result<Unit> = try {
        reviewDataSource.updateReview(id, dto)
        _reviewsChanged.tryEmit(Unit)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo actualizar la reseña $id", e)
        Result.failure(e)
    }

    suspend fun deleteReview(id: String): Result<Unit> = try {
        reviewDataSource.deleteReview(id)
        _reviewsChanged.tryEmit(Unit)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "No se pudo eliminar la reseña $id", e)
        Result.failure(e)
    }

    private companion object {
        const val TAG = "ReviewRepository"
    }
}