package com.example.kloth.data.dto

/**
 * Estructura exacta de un usuario (`GET /users`, `GET /users/:id`).
 * El campo `password` del backend se ignora deliberadamente: nunca debe
 * llegar hasta la capa de red del cliente.
 */
data class UserDto(
    val userId: Int,
    val email: String? = null,
    val username: String,
    val imageURL: String? = null,
    val biography: String? = null,
    val registeredAt: String? = null,
    val userType: String? = null
)
