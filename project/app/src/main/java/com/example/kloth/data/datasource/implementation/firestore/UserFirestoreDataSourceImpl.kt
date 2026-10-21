package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.services.UserRemoteDataSource
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDtoGeneric
import com.example.kloth.data.dto.UserFirestoreDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.ArticleFirestoreDto
import com.example.kloth.data.local.UserFollowState
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore): UserRemoteDataSource {
    override suspend fun getAllUsers(): List<UserDtoGeneric> {
        return db.collection("users").get().await().documents.mapNotNull { doc ->
            doc.toObject(UserFirestoreDto::class.java)?.copy(userId = doc.id)
        }
    }

    override suspend fun getUserById(id: String): UserDtoGeneric {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()
        return respuesta.toObject(UserFirestoreDto::class.java)?.copy(userId = respuesta.id)
            ?: throw Exception("No se pudo obtener el usuario")
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        //Se quiere ingresar a la coleccion de usuarios
        //En la coleccion de usuarios cree un nuevo documento que tendrá como PK el user ID
        //A ese documento insertele el usuario registerUserDto
        val docRef = db.collection("users").document(userId)
        docRef.set(registerUserDto).await()
    }
    override suspend fun getUserArticles(userId: String): List<ArticleDtoGeneric> {
        return db.collection("articles")
            .whereEqualTo("idUser", userId)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(ArticleFirestoreDto::class.java)?.copy(articleId = doc.id)
            }
    }

    override suspend fun updateProfileImage(userId: String, imageUrl: String) {
        db.collection("users").document(userId).update("imageURL", imageUrl).await()
    }

    suspend fun updateFcmToken(userId: String, token: String) {
        db.collection("users").document(userId)
            .set(mapOf("fcmToken" to token), SetOptions.merge())
            .await()
    }

    override suspend fun updateProfile(
        userId: String,
        username: String,
        biography: String?,
        location: String?,
        website: String?,
        email: String?
    ) {
        // Solo se escriben los campos editables del formulario. El resto del documento
        // (fullName, password, imagen, fechas...) NO se toca: el merge conserva esos valores
        // y por eso los campos que el usuario no editó nunca desaparecen.
        val cambiosBase = mapOf<String, Any>(
            "username" to username.trim(),
            "biography" to (biography?.trim()?.takeIf { it.isNotEmpty() } ?: FieldValue.delete()),
            "location" to (location?.trim()?.takeIf { it.isNotEmpty() } ?: FieldValue.delete()),
            "website" to (website?.trim()?.takeIf { it.isNotEmpty() } ?: FieldValue.delete()),
            "updatedAt" to Instant.now().toString()
        )
        val cambios = if (email.isNullOrBlank()) cambiosBase else cambiosBase + ("email" to email.trim())

        db.collection("users").document(userId).set(cambios, SetOptions.merge()).await()
    }

    suspend fun isFollowing(currentUserId: String, targetUserId: String): Boolean {
        return db.collection("users").document(currentUserId)
            .collection("following").document(targetUserId).get().await().exists()
    }

    suspend fun getFollowingIds(userId: String): Set<String> {
        return db.collection("users").document(userId).collection("following")
            .get().await().documents.map { it.id }.toSet()
    }

    suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String): UserFollowState {
        val currentUserRef = db.collection("users").document(currentUserId)
        val targetUserRef = db.collection("users").document(targetUserId)
        val followingRef = currentUserRef.collection("following").document(targetUserId)
        val followersRef = targetUserRef.collection("followers").document(currentUserId)

        return db.runTransaction { transaction ->
            val followingSnapshot = transaction.get(followingRef)
            val targetSnapshot = transaction.get(targetUserRef)
            val currentFollowersCount = targetSnapshot.getLong("followersCount")?.toInt() ?: 0
            if (followingSnapshot.exists()) {
                transaction.delete(followingRef)
                transaction.delete(followersRef)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(-1))
                UserFollowState(
                    isFollowing = false,
                    followersCount = currentFollowersCount - 1
                )
            } else {
                val data = mapOf("timestamp" to FieldValue.serverTimestamp())
                transaction.set(followingRef, data)
                transaction.set(followersRef, data)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(1))
                UserFollowState(isFollowing = true, followersCount = currentFollowersCount + 1)
            }
        }.await()
    }
}
