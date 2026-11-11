package com.example.kloth.data.dto

import com.example.kloth.data.local.UserProfileInfo
import com.google.firebase.firestore.IgnoreExtraProperties

abstract class UserDtoGeneric {
    abstract fun toUserProfileInfo(): UserProfileInfo
}
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
            profileImageUrl = imageURL,
            location =  null,
            website = null
        )
    }
}
@IgnoreExtraProperties
data class UserFirestoreDto(
    val userId: String = "",
    val email: String = "",
    // Se guarda en texto plano por requisito. No se usa para autenticar: eso lo hace Firebase Auth.
    val password: String = "",
    val username: String = "",
    val fullName: String = "",
    val imageURL: String? = null,
    val biography: String? = null,
    val location: String? = null,
    val website: String? = null,
    // Algunos documentos históricos guardan estas fechas como String y otros como Timestamp.
    // La UI no las consume; conservar el valor sin forzar un único formato evita fallar al leerlos.
    val registeredAt: Any? = null,
    val eliminated: Boolean = false,
    val eliminatedAt: Any? = null,
    val userType: String = "USER",
    val createdAt: Any? = null,
    val updatedAt: Any? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0
) : UserDtoGeneric() {

    override fun toUserProfileInfo(): UserProfileInfo {
        return UserProfileInfo(
            id = userId,
            username = username.ifBlank { fullName },
            email = email,
            bio = biography,
            location = location,
            website = website,
            profileImageUrl = imageURL,
            followersCount = followersCount,
            followingCount = followingCount
        )
    }
}
