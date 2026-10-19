package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDtoGeneric

interface UserRemoteDataSource {
    suspend fun getAllUsers(): List<UserDtoGeneric>
    suspend fun getUserById(id: String): UserDtoGeneric
    suspend fun registerUser(registerUserDto: RegisterUserDto, userId : String): Unit
    suspend fun getUserArticles(userId: String): List<ArticleDtoGeneric>
}
