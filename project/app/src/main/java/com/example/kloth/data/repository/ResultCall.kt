package com.example.kloth.data.repository

import retrofit2.HttpException
import java.io.IOException

/**
 * Ejecuta una llamada de red y la empaqueta en [Result] sin exponer clases
 * de infraestructura (no se devuelve `Response<T>` de Retrofit).
 *
 * - `HttpException` -> error HTTP con código (404, 500, ...).
 * - `IOException` -> dispositivo sin conexión al backend.
 */
internal suspend fun <T> networkResult(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: HttpException) {
        Result.failure(Exception("Error de servidor HTTP: ${e.code()}"))
    } catch (e: IOException) {
        Result.failure(Exception("Sin conexión con el servidor"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
