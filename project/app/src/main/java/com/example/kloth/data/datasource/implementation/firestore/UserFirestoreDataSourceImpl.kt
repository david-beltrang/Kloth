package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.services.UserRemoteDataSource
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDtoGeneric
import com.example.kloth.data.dto.UserFirestoreDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.ArticleFirestoreDto
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class   UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore): UserRemoteDataSource {
    override suspend fun getAllUsers(): List<UserDtoGeneric> {
        return try {
            db.collection("users").get().await().documents.mapNotNull { doc ->
                doc.toObject(UserFirestoreDto::class.java)?.copy(userId = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getUserById(id: String, currentUserId: String): UserDtoGeneric {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()
        val user = respuesta.toObject(UserFirestoreDto::class.java)?.copy(userId = respuesta.id)
            ?: throw Exception("No se pudo obtener el usuario")

        if (currentUserId.isNotEmpty()) {
            // Verifica si el usuario actual ya sigue a este usuario
            val followerDoc = docRef.collection("followers").document(currentUserId).get().await()
            val exist = followerDoc.exists()
            user.followed = exist
        }
        return user
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

    override suspend fun updateProfile(
        userId: String,
        username: String,
        biography: String?,
        location: String?,
        website: String?
    ) {
        // Solo se tocan estos campos; merge conserva el resto del documento
        val cambios = mapOf(
            "username" to username,
            "biography" to biography,
            "location" to location,
            "website" to website
        )
        db.collection("users").document(userId).set(cambios, SetOptions.merge()).await()
    }

    override suspend fun followOrUnfollow(currentUserId: String, targetUserId: String) {
        val currentUserRef = db.collection("users").document(currentUserId)
        val targetUserRef = db.collection("users").document(targetUserId)

        val followingRef = currentUserRef.collection("following").document(targetUserId)
        val followerRef = targetUserRef.collection("followers").document(currentUserId)

        db.runTransaction { transaction ->
            val followingDoc = transaction.get(followingRef)

            if (followingDoc.exists()) {
                // Dejar de seguir
                transaction.delete(followingRef)
                transaction.delete(followerRef)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(-1))
            } else {
                // Seguir
                transaction.set(followingRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.set(followerRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(1))
            }
        }.await()
    }
}