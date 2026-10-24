package com.example.kloth.data.datasource.implementation.retrofit

import com.example.kloth.data.datasource.services.UserRemoteDataSource
import com.example.kloth.data.datasource.services.UserRetrofitService
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserRetrofitDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {
    override suspend fun getAllUsers(): List<UserRetrofitDto> = service.getUsers()
    override suspend fun getUserById(id: String): UserRetrofitDto = service.getUserById("1")
    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        TODO("falta por implementar epara retrofit")
    }

    override suspend fun getUserArticles(userId: String): List<ArticleDtoGeneric> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfileImage(userId: String, imageUrl: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(
        userId: String,
        username: String,
        biography: String?,
        location: String?,
        website: String?,
        email: String?
    ) {
        TODO("Not yet implemented")
    }
}
