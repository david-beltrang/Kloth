package com.example.kloth.data.repository

import android.content.Context
import com.example.kloth.R
import com.example.kloth.data.datasource.services.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctionsException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    @ApplicationContext private val context: Context
){

    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    suspend fun signIn (email: String, pasword: String): Result<Unit>{
        return try{
            authRemoteDataSource.signIn(email, pasword)
            Result.success(Unit)
        }catch (e: FirebaseAuthInvalidCredentialsException){
            Result.failure(Exception(context.getString(R.string.auth_error_invalid_credentials)))
        } catch(e: FirebaseAuthInvalidUserException){
            Result.failure(Exception(context.getString(R.string.auth_error_user_not_found)))
        }
        catch (e: Exception){
            Result.failure(Exception(context.getString(R.string.login_error_generic)))
        }
    }

    suspend fun signUp(email: String,password: String): Result<Unit> {
        return try{
            authRemoteDataSource.signUp(email, password)
            Result.success(Unit)
        }catch(e: FirebaseAuthUserCollisionException){
            Result.failure(Exception(context.getString(R.string.auth_error_email_already_registered)))
        }catch(e: FirebaseAuthWeakPasswordException){
            Result.failure(Exception(context.getString(R.string.auth_error_weak_password)))
        }catch(e: FirebaseAuthInvalidCredentialsException){
            Result.failure(Exception(context.getString(R.string.auth_error_invalid_email)))
        } catch(e: Exception){
            Result.failure(Exception(context.getString(R.string.register_error_generic)))
        }
    }

    fun signOut(){
        authRemoteDataSource.signOut()
    }

    /**
     * Actualiza el email en Firebase Auth. Firebase exige que el usuario se haya
     * autenticado recientemente, por eso el error se devuelve al ViewModel.
     */
    suspend fun updateEmail(email: String): Result<Unit> {
        return try {
            authRemoteDataSource.updateEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun requestPasswordResetCode(email: String): Result<Unit> = try {
        authRemoteDataSource.requestPasswordResetCode(email)
        Result.success(Unit)
    } catch (e: FirebaseFunctionsException) {
        Result.failure(Exception(requestCodeErrorMessage(e)))
    } catch (e: Exception) {
        Result.failure(Exception(context.getString(R.string.auth_error_generic)))
    }

    suspend fun resetPasswordWithCode(email: String, code: String, newPassword: String): Result<Unit> = try {
        authRemoteDataSource.resetPasswordWithCode(email, code, newPassword)
        Result.success(Unit)
    } catch (e: FirebaseFunctionsException) {
        Result.failure(Exception(functionsErrorMessage(e)))
    } catch (e: Exception) {
        Result.failure(Exception(context.getString(R.string.auth_error_generic)))
    }

    private fun functionsErrorMessage(exception: FirebaseFunctionsException): String = when (exception.code) {
        FirebaseFunctionsException.Code.INVALID_ARGUMENT -> context.getString(R.string.auth_error_invalid_reset_code)
        FirebaseFunctionsException.Code.NOT_FOUND -> context.getString(R.string.auth_error_invalid_reset_code)
        FirebaseFunctionsException.Code.RESOURCE_EXHAUSTED -> context.getString(R.string.auth_error_reset_wait)
        FirebaseFunctionsException.Code.UNAVAILABLE,
        FirebaseFunctionsException.Code.DEADLINE_EXCEEDED -> context.getString(R.string.auth_error_connection)
        else -> context.getString(R.string.auth_error_generic)
    }

    private fun requestCodeErrorMessage(exception: FirebaseFunctionsException): String = when (exception.code) {
        FirebaseFunctionsException.Code.INVALID_ARGUMENT -> context.getString(R.string.auth_error_invalid_email)
        FirebaseFunctionsException.Code.RESOURCE_EXHAUSTED -> context.getString(R.string.auth_error_reset_wait)
        FirebaseFunctionsException.Code.UNAVAILABLE,
        FirebaseFunctionsException.Code.DEADLINE_EXCEEDED -> context.getString(R.string.auth_error_connection)
        else -> context.getString(R.string.auth_error_generic)
    }
}

