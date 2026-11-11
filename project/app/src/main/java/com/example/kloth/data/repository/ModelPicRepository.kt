package com.example.kloth.data.repository

import android.net.Uri
import android.util.Log
import com.example.kloth.data.datasource.services.ModelPicRemoteDataSource
import com.example.kloth.data.local.ModelPic
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelPicRepository @Inject constructor(
    private val modelPicDataSource: ModelPicRemoteDataSource,
    private val storageRepository: StorageRepository
) {
    suspend fun getModelPics(userId: String): Result<List<ModelPic>> = try {
        val modelPics = modelPicDataSource.getModelPics(userId)
            .mapNotNull { it.toModelPic(ownerId = userId) }
        Result.success(modelPics)
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        Log.e(TAG, "No se pudieron cargar las model pics del usuario $userId", error)
        Result.failure(error)
    }

    suspend fun uploadBaseModelPic(
        userId: String,
        name: String,
        imageUri: Uri,
        contentType: String?
    ): Result<String> = try {
        if (userId.isBlank()) throw IllegalArgumentException("El ID del usuario es obligatorio")
        if (name.isBlank()) throw IllegalArgumentException("El nombre de la imagen es obligatorio")

        val uploadedImage = storageRepository.uploadModelPicImage(userId, imageUri, contentType)
            .getOrElse { throw it }

        try {
            val id = modelPicDataSource.createBaseModelPic(
                userId,
                name.trim(),
                uploadedImage.imageUrl,
                uploadedImage.storagePath
            )
            Result.success(id)
        } catch (error: Exception) {
            storageRepository.deleteImage(uploadedImage.imageUrl)
                .exceptionOrNull()
                ?.let(error::addSuppressed)
            throw error
        }
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        Log.e(TAG, "No se pudo subir la model pic base del usuario $userId", error)
        Result.failure(error)
    }

    private companion object {
        const val TAG = "ModelPicRepository"
    }
}
