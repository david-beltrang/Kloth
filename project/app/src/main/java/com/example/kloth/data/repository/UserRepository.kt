package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDtoGeneric
import com.example.kloth.data.local.UserProfileInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserFirestoreDataSourceImpl
) {
    private val _usersChanged = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val usersChanged: SharedFlow<Unit> = _usersChanged.asSharedFlow()
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

    suspend fun updateProfile(
        userId: String,
        username: String,
        biography: String,
        location: String,
        website: String
    ): Result<Unit> {
        return try {
            // Los campos opcionales vacíos se guardan como null
            userDataSource.updateProfile(
                userId = userId,
                username = username.trim(),
                biography = biography.trim().ifBlank { null },
                location = location.trim().ifBlank { null },
                website = website.trim().ifBlank { null }
            )
            // Avisa a perfil, feed y reseñas para que se recarguen
            _usersChanged.tryEmit(Unit)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepository", "No se pudo actualizar el perfil", e)
            Result.failure(e)
        }
    }

    suspend fun updateProfileImage(userId: String, imageUrl: String): Result<Unit> {
        return try {
            userDataSource.updateProfileImage(userId, imageUrl)
            _usersChanged.tryEmit(Unit)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
