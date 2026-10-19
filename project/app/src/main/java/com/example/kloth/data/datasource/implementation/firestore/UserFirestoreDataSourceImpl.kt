package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.services.UserRemoteDataSource
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDtoGeneric
import com.example.kloth.data.dto.UserFirestoreDto
import com.google.firebase.firestore.FirebaseFirestore
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
}