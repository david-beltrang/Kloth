package com.example.kloth.data.dto

import com.example.kloth.data.local.UserProfileInfo

/** Contrato: todo DTO de usuario, venga de Retrofit o de Firestore, sabe convertirse al modelo de la UI. */
abstract class UserDtoGeneric {
    abstract fun toUserProfileInfo(): UserProfileInfo
}

/** Copia exacta del JSON de GET /users y GET /users/{id} (modelo User del backend). */
data class UserRetrofitDto(
    val userId: Int,
    val email: String,
    val username: String,
    val imageURL: String?,
    val biography: String?,
    val registeredAt: String?,   // DATEONLY: "2026-09-20"
    val eliminated: Boolean,
    val eliminatedAt: String?,
    val userType: String,        // "ADMIN" o "USER"
    val createdAt: String?,
    val updatedAt: String?
) : UserDtoGeneric() {

    override fun toUserProfileInfo(): UserProfileInfo {
        return UserProfileInfo(
            id = userId.toString(),
            username = username,
            email = email,
            bio = biography,
            profileImageUrl = imageURL
        )
    }
}

/** Documento de la colección "users" de Firestore. El id del documento es el uid de Firebase Auth. */
data class UserFirestoreDto(
    val userId: String,          // uid de Firebase Auth: en Firestore los ids son texto
    val email: String,
    val username: String,
    val imageURL: String?,
    val biography: String?,
    val registeredAt: String?,
    val eliminated: Boolean,
    val eliminatedAt: String?,
    val userType: String,
    val createdAt: String?,
    val updatedAt: String?
) : UserDtoGeneric() {
    constructor() : this("", "", "", null, null, null, false, null, "USER", null, null)

    override fun toUserProfileInfo(): UserProfileInfo {
        return UserProfileInfo(
            id = userId,
            username = username,
            email = email,
            bio = biography,
            profileImageUrl = imageURL
        )
    }
}