package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.dto.ModelPicFirestoreDto
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.data.datasource.services.ModelPicRemoteDataSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class ModelPicFirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : ModelPicRemoteDataSource {
    override suspend fun getModelPics(userId: String): List<ModelPicFirestoreDto> {
        if (userId.isBlank()) throw IllegalArgumentException("El ID del usuario es obligatorio")

        return db.collection(USERS_COLLECTION)
            .document(userId)
            .collection(MODEL_PICS_COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(ModelPicFirestoreDto::class.java)
                    ?.copy(modelPicId = document.id)
            }
            .sortedByDescending { it.createdAt }
    }

    override suspend fun createBaseModelPic(
        userId: String,
        name: String,
        imageUrl: String,
        storagePath: String
    ): String {
        if (userId.isBlank()) throw IllegalArgumentException("El ID del usuario es obligatorio")
        if (name.isBlank()) throw IllegalArgumentException("El nombre de la imagen es obligatorio")
        if (imageUrl.isBlank()) throw IllegalArgumentException("La URL de la imagen es obligatoria")
        if (storagePath.isBlank()) throw IllegalArgumentException("La ruta de Storage es obligatoria")

        val document = db.collection(USERS_COLLECTION)
            .document(userId)
            .collection(MODEL_PICS_COLLECTION)
            .document()

        document.set(
            mapOf(
                "name" to name.trim(),
                "imageUrl" to imageUrl.trim(),
                "storagePath" to storagePath.trim(),
                "type" to ModelPicType.BASE.firestoreValue,
                "sourcePublicationId" to null,
                "baseModelPicId" to null,
                "createdAt" to FieldValue.serverTimestamp()
            )
        ).await()

        return document.id
    }

    private companion object {
        const val USERS_COLLECTION = "users"
        const val MODEL_PICS_COLLECTION = "modelPics"
    }
}
