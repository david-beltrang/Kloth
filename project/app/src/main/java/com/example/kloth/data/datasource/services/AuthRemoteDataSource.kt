package com.example.kloth.data.datasource.services

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
){

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun updateProfileImage(photoUrl: String): Unit {
        val user = currentUser ?: return
        val uri = photoUrl.toUri()
        user.updateProfile(
            UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
        ).await()
        user.reload().await()
    }

    suspend fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).await()
    }

    /** Cambia el email del usuario en Firebase Auth. Pide inicio de sesión reciente. */
    suspend fun updateEmail(email: String) {
        val user = currentUser ?: throw Exception("No hay usuario autenticado")
        user.updateEmail(email).await()
        user.reload().await()
    }

    suspend fun signUp(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun signOut(){
        auth.signOut()
    }
}