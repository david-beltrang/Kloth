package com.example.kloth.data.repository

import android.content.Context
import android.net.Uri
import com.example.kloth.R
import com.example.kloth.data.datasource.services.AuthRemoteDataSource
import com.example.kloth.data.datasource.services.StorageRemoteDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
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
            val baseUrl = storage.uploadImage(path, uri)
            val url = if (baseUrl.contains("?")) "$baseUrl&v=${System.currentTimeMillis()}" else "$baseUrl?v=${System.currentTimeMillis()}"

            auth.updateProfileImage(url)
            Result.success(url)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun uploadArticleImage(uri: Uri): Result<String> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception(context.getString(R.string.storage_error_user_not_logged_in)))

            val path = "articleImages/$userId/${UUID.randomUUID()}.jpg"
            val url = storage.uploadImage(path, uri)
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteImage(url: String): Result<Unit> = try {
        storage.deleteImage(url)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}