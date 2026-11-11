package com.example.kloth.data.datasource.implementation.firebase

import com.example.kloth.data.datasource.services.TryOnRemoteDataSource
import com.example.kloth.data.dto.TryOnResultDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class TryOnFirebaseDataSource @Inject constructor(
    private val functions: FirebaseFunctions,
    private val auth: FirebaseAuth
) : TryOnRemoteDataSource {
    override suspend fun generateTryOn(
        publicationId: String,
        baseModelPicId: String
    ): TryOnResultDto {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No hay una sesión de Firebase Auth activa")
        // Refresca el token antes de invocar para evitar enviar una sesión vieja (p. ej. tras cambiar
        // entre Debug con emulador y Release con Firebase real). Firebase Functions adjunta el token.
        currentUser.getIdToken(true).await()

        val response = functions
            .getHttpsCallable(FUNCTION_NAME)
            .withTimeout(CALLABLE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .call(
                mapOf(
                    "publicationId" to publicationId,
                    "baseModelPicId" to baseModelPicId
                )
            )
            .await()

        val data = response.getData() as? Map<*, *>
            ?: throw IllegalStateException("La respuesta de Try On no tiene el formato esperado")
        val modelPicId = data["modelPicId"] as? String
            ?: throw IllegalStateException("La respuesta de Try On no contiene modelPicId")
        val imageUrl = data["imageUrl"] as? String
            ?: throw IllegalStateException("La respuesta de Try On no contiene imageUrl")

        return TryOnResultDto(modelPicId = modelPicId, imageUrl = imageUrl)
    }

    private companion object {
        const val FUNCTION_NAME = "generateTryOn"
        const val CALLABLE_TIMEOUT_SECONDS = 300L
    }
}
