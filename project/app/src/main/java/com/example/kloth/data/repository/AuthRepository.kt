package com.example.kloth.data.repository

import android.content.Context
import android.net.Uri
import com.example.kloth.R
import com.example.kloth.data.dataresource.AuthRemoteDataSource
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordState
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
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


    //suspend fun forgotPassword(email: String){
    //    authRemoteDataSource.forgotPassword(email)
    //}
}

