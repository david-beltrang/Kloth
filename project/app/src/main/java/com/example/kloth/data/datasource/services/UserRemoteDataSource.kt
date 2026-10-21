package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDtoGeneric

interface UserRemoteDataSource {
    suspend fun getAllUsers(): List<UserDtoGeneric>
    suspend fun getUserById(id: String, currentUserId: String = ""): UserDtoGeneric
    suspend fun registerUser(registerUserDto: RegisterUserDto, userId : String): Unit
    suspend fun getUserArticles(userId: String): List<ArticleDtoGeneric>
    suspend fun updateProfileImage(userId: String, imageUrl: String)
    suspend fun updateProfile(
        userId: String,
        username: String,
        biography: String?,
        location: String?,
        website: String?
    )

    //nuevos
    suspend fun followOrUnfollow(currentUserId: String, targetUserId: String): Unit
}
