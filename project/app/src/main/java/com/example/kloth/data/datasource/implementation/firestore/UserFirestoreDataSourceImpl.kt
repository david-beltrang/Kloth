package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.UserRemoteDataSource
import com.example.kloth.data.dto.RegisterUserDto
import com.example.kloth.data.dto.UserDto
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore): UserRemoteDataSource {
    override suspend fun getAllUsers(): List<UserDto> {
        TODO()
    }

    override suspend fun getUserById(id: String): UserDto {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()
        return respuesta.toObject(UserDto::class.java) ?: throw Exception("No se pudo obtener el usuario")
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
}