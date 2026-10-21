package com.example.kloth.data.local


data class UserProfileInfo(
    val id: String,
    val username: String?,
    val email: String,
    val bio: String?,
    val location: String?,
    val website: String?,
    val profileImageUrl: String?,
    val followers: Int = 0,
    val following: Int = 0,
    val followed: Boolean = false
)