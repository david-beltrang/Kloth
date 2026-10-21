package com.example.kloth.data.dto

import com.example.kloth.data.local.UserProfileInfo

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
data class UserFirestoreDto(
    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val fullName: String = "",
    val imageURL: String? = null,
    val biography: String? = null,
    val location: String? = null,
    val website: String? = null,
    val registeredAt: String? = null,
    val eliminated: Boolean = false,
    val eliminatedAt: String? = null,
    val userType: String = "USER",
    val createdAt: String? = null,
    val updatedAt: String? = null,
    // En 0 por defecto: los usuarios creados antes del sprint 11 no tienen estos campos
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    //nuevo
    var followed: Boolean = false
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
            followers = followersCount,
            following = followingCount,
            followed = followed
        )
    }
}