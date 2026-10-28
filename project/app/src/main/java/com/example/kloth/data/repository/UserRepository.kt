package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDtoGeneric
import com.example.kloth.data.local.UserProfileInfo
import com.example.kloth.data.local.UserFollowState
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserFirestoreDataSourceImpl
) {
    suspend fun getAllUsers(): List<UserDtoGeneric> = userDataSource.getAllUsers()

    suspend fun getFollowingIds(userId: String): Result<Set<String>> = try {
        Result.success(userDataSource.getFollowingIds(userId))
    } catch (e: Exception) {
        Log.e("UserRepository", "No se pudieron cargar los usuarios seguidos", e)
        Result.failure(e)
    }

    suspend fun getFollowedUsers(userId: String, listType: String): Result<List<UserProfileInfo>> = try {
        Result.success(userDataSource.getFollowedUsers(userId, listType).map { it.toUserProfileInfo() })
    } catch (e: Exception) {
        Log.e("UserRepository", "No se pudo cargar la lista de seguimiento", e)
        Result.failure(e)
    }

    suspend fun isFollowing(currentUserId: String, targetUserId: String): Result<Boolean> = try {
        Result.success(userDataSource.isFollowing(currentUserId, targetUserId))
    } catch (e: Exception) {
        Log.e("UserRepository", "No se pudo consultar el seguimiento", e)
        Result.failure(e)
    }

    suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String): Result<UserFollowState> = try {
        Result.success(userDataSource.followOrUnfollowUser(currentUserId, targetUserId))
    } catch (e: Exception) {
        Log.e("UserRepository", "No se pudo actualizar el seguimiento", e)
        Result.failure(e)
    }

    suspend fun removeFollower(ownerUserId: String, followerUserId: String): Result<Unit> = try {
        userDataSource.removeFollower(ownerUserId, followerUserId)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("UserRepository", "No se pudo eliminar al seguidor $followerUserId", e)
        Result.failure(e)
    }

    suspend fun getUserById(id: String): Result<UserProfileInfo> {
        return try {
            val user = userDataSource.getUserById(id)
            val userProfileInfo = user.toUserProfileInfo()
            Result.success(userProfileInfo)
        } catch (e: Exception) {
            Log.e("UserRepository", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun registerUser(
        email: String,
        password: String,
        fullName: String,
        userId: String,
        imageURL: String? = null,
        biography: String? = null,
        location: String? = null,
        website: String? = null
    ): Result<Unit> {
        return try {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            val now = Instant.now().toString()
            val registerUserDto = RegisterUserDto(
                email = email.trim(),
                password = password,
                fullName = fullName.trim(),
                username = buildUsername(fullName, userId),
                imageURL = imageURL,
                biography = biography,
                location = location,
                website = website,
                registeredAt = now,
                createdAt = now,
                updatedAt = now,
                fcmToken = fcmToken
            )
            userDataSource.registerUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Genera el username a partir del nombre completo: "Ana María Pérez"  "ana_maría_pérez".
     * Si el nombre no deja nada utilizable, se usa un alias con el id del usuario.
     */
    private fun buildUsername(fullName: String, userId: String): String =
        fullName.trim().lowercase()
            .split(" ")
            .filter { it.isNotEmpty() }
            .joinToString("_")
            .ifBlank { "user_${userId.take(6)}" }

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
        website: String,
        email: String? = null
    ): Result<Unit> {
        return try {
            // El merge del datasource conserva los campos que no se editan (fullName, password...)
            userDataSource.updateProfile(
                userId = userId,
                username = username.trim(),
                biography = biography.trim().ifBlank { null },
                location = location.trim().ifBlank { null },
                website = website.trim().ifBlank { null },
                email = email?.trim()?.takeIf { it.isNotEmpty() }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepository", "No se pudo actualizar el perfil", e)
            Result.failure(e)
        }
    }

    suspend fun updateProfileImage(userId: String, imageUrl: String): Result<Unit> {
        return try {
            userDataSource.updateProfileImage(userId, imageUrl)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateFcmToken(userId: String, token: String): Result<Unit> = try {
        userDataSource.updateFcmToken(userId, token)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("UserRepository", "No se pudo actualizar el token de notificaciones", e)
        Result.failure(e)
    }
}
