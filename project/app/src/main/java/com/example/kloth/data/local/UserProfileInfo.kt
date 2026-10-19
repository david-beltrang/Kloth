package com.example.kloth.data.local


data class UserProfileInfo(
    val id: String,
    val username: String?,
    val email: String,
    val bio: String?,
    val profileImageUrl: String?
)