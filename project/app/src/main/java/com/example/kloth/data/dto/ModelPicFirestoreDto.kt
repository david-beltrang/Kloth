package com.example.kloth.data.dto

import com.example.kloth.data.local.ModelPic
import com.example.kloth.data.local.ModelPicType
import com.google.firebase.Timestamp

/**
 * Documento de users/{userId}/modelPics/{modelPicId}.
 * La imagen vive en Storage; Firestore conserva su URL y su ruta para poder administrarla.
 */
data class ModelPicFirestoreDto(
    val modelPicId: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val storagePath: String = "",
    val type: String = ModelPicType.BASE.firestoreValue,
    val sourcePublicationId: String? = null,
    val baseModelPicId: String? = null,
    val createdAt: Timestamp? = null
) {
    fun toModelPic(ownerId: String): ModelPic? {
        val modelPicType = ModelPicType.fromFirestore(type) ?: return null
        return ModelPic(
            id = modelPicId,
            ownerId = ownerId,
            name = name,
            imageUrl = imageUrl,
            storagePath = storagePath,
            type = modelPicType,
            sourcePublicationId = sourcePublicationId,
            baseModelPicId = baseModelPicId,
            createdAt = createdAt
        )
    }
}
