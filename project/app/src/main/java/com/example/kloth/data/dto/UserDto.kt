package com.example.kloth.data.dto

// DTO de entrada: copia exacta del JSON que devuelve GET /users y GET /users/{id}
data class UserDto(
    val userId: Int,
    val email: String,
    val username: String,
    val password: String,
    val imageURL: String?,
    val biography: String?,
    val registeredAt: String?,
    val eliminated: Boolean,
    val eliminatedAt: String?,
    val userType: String,
    val createdAt: String?,
    val updatedAt: String?
)
