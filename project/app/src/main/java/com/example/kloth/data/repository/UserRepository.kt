package com.example.kloth.data.repository

import com.example.kloth.data.dataresource.UserRemoteDataSource
import com.example.kloth.data.dto.UserDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Usuarios del backend. Mantiene un mapa id -> usuario en caché para
 * resolver autores de reseñas/comentarios/publicaciones sin N+1 llamadas.
 */
@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {

    private var cachedUsers: Map<Int, UserDto>? = null

    /**
     * Mapa id -> usuario. Si la red falla devuelve un mapa vacío para que el
     * contenido (artículos/reseñas) no se rompa por un problema de autores.
     */
    suspend fun getUsersMap(forceRefresh: Boolean = false): Map<Int, UserDto> {
        cachedUsers?.let { if (!forceRefresh) return it }
        val result = try {
            Result.success(remoteDataSource.getAllUsers())
        } catch (e: Exception) {
            Result.failure(e)
        }
        val map = result.getOrElse { return emptyMap() }
            .associateBy { it.userId }
        cachedUsers = map
        return map
    }

    suspend fun getUserById(id: String): Result<UserDto> {
        return try {
            Result.success(remoteDataSource.getUserById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
