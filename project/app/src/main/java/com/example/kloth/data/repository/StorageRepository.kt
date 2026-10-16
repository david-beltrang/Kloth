package com.example.kloth.data.repository

import android.content.Context
import android.net.Uri
import com.example.kloth.R
import com.example.kloth.data.dataresource.AuthRemoteDataSource
import com.example.kloth.data.dataresource.StorageRemoteDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRemoteDataSource,
    @ApplicationContext private val context: Context
){

    suspend fun uploadProfileImage(uri: Uri): Result<String> {
        return try{
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception(context.getString(R.string.storage_error_user_not_logged_in)))

            val path = "profileImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)

            auth.updateProfileImage(url)
            Result.success(url)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}