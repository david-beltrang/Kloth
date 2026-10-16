package com.example.kloth.data.repository

import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDto
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserFirestoreDataSourceImpl
) {
    suspend fun getAllUsers(): List<UserDto> = userDataSource.getAllUsers()

    suspend fun getUserById(id: String): UserDto = userDataSource.getUserById(id)

    suspend fun registerUser(
        fullName: String,
        email: String,
        password: String,
        userId: String,
        imageURL: String? = null,
        biography: String? = null,
        location: String? = null,
        website: String? = null
    ): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(
                fullName = fullName,
                email = email,
                password = password,
                imageURL = imageURL,
                biography = biography,
                location = location,
                website = website
            )
            userDataSource.registerUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
