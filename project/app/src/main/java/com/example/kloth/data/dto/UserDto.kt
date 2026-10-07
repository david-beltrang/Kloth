package com.example.kloth.data.dto

// DTO de entrada: copia del JSON que devuelve GET /users y GET /users/{id}.
// El backend tambien envia "password": se omite a proposito para no guardarla en la app
// (Gson ignora los campos del JSON que no estan en el DTO).
data class UserDto(
    val userId: Int,
    val email: String,
    val username: String,
    val imageURL: String?,
    val biography: String?,
    val registeredAt: String?,
    val eliminated: Boolean,
    val eliminatedAt: String?,
    val userType: String,
    val createdAt: String?,
    val updatedAt: String?
)
