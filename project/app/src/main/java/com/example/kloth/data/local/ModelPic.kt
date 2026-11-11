package com.example.kloth.data.local

import com.google.firebase.Timestamp

enum class ModelPicType(val firestoreValue: String) {
    BASE("base"),
    GENERATED("generated");

    companion object {
        fun fromFirestore(value: String): ModelPicType? =
            entries.firstOrNull { it.firestoreValue == value }
    }
}

data class ModelPic(
    val id: String,
    val ownerId: String,
    val name: String,
    val imageUrl: String,
    val storagePath: String,
    val type: ModelPicType,
    val sourcePublicationId: String? = null,
    val baseModelPicId: String? = null,
    val createdAt: Timestamp? = null
)
