package com.example.kloth.data.repository

import com.example.kloth.data.dataresource.AuthRemoteDataSource
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordState
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
){

    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    suspend fun signIn (email: String, pasword: String): Result<Unit>{
        return try{
            authRemoteDataSource.signIn(email, pasword)
            Result.success(Unit)
        }catch (e: FirebaseAuthInvalidCredentialsException){
            Result.failure(Exception("Credenciales incorrectas"))
        } catch(e: FirebaseAuthInvalidUserException){
            Result.failure(Exception("El usuario no existe"))
        }
        catch (e: Exception){
            Result.failure(Exception("Error al iniciar sesion"))
        }
    }

    suspend fun signUp(email: String,password: String){
        authRemoteDataSource.signUp(email, password)
    }

    fun signOut(){
        authRemoteDataSource.signOut()
    }

    //suspend fun forgotPassword(email: String){
    //    authRemoteDataSource.forgotPassword(email)
    //}
}

