package com.example.kloth.data.repository

import android.util.Log
import com.example.kloth.data.datasource.services.TryOnRemoteDataSource
import com.example.kloth.data.local.TryOnResult
import com.google.firebase.functions.FirebaseFunctionsException
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TryOnRepository @Inject constructor(
    private val tryOnDataSource: TryOnRemoteDataSource
) {
    suspend fun generateTryOn(
        publicationId: String,
        baseModelPicId: String
    ): Result<TryOnResult> = try {
        val result = tryOnDataSource.generateTryOn(publicationId, baseModelPicId)
            .toTryOnResult()
        Result.success(result)
    } catch (error: CancellationException) {
        throw error
    } catch (error: FirebaseFunctionsException) {
        Log.e(
            TAG,
            "Callable generateTryOn falló: code=${error.code}, details=${error.details}",
            error
        )
        Result.failure(error)
    } catch (error: Exception) {
        Log.e(TAG, "No se pudo generar el Try On para la publicación $publicationId", error)
        Result.failure(error)
    }

    private companion object {
        const val TAG = "TryOnRepository"
    }
}
