package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDtoGeneric
import com.example.kloth.data.local.UserProfileInfo
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserFirestoreDataSourceImpl
) {
    suspend fun getAllUsers(): List<UserDtoGeneric> = userDataSource.getAllUsers()

    suspend fun getUserById(id: String): Result<UserProfileInfo> {
        return try {
            val user = userDataSource.getUserById(id)
            val userProfileInfo = user.toUserProfileInfo()
            Result.success(userProfileInfo)
        } catch (e: Exception) {
            Log.e("TAG", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun registerUser(
        fullName: String,
        userId: String,
        imageURL: String? = null,
        biography: String? = null,
        location: String? = null,
        website: String? = null
    ): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(
                fullName = fullName,
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

    suspend fun getUserArticles(userId: String): Result<List<ArticleDtoGeneric>> {
        return try {
            val articles = userDataSource.getUserArticles(userId)
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
