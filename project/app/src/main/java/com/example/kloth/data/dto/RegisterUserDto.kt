package com.example.kloth.data.dto

/**
 * Documento que se crea en `users/{uid}` al registrarse.
 *
 * - `email`, `password` y `fullName` vienen del formulario de registro.
 * - `username` se deriva del nombre y el usuario puede cambiarlo en Edit Profile.
 *
 * Nota: la contraseña se guarda en texto plano porque así lo pide el requisito actual.
 * Firebase Auth sigue siendo la fuente de verdad para autenticarse.
 */
data class RegisterUserDto(
    val email: String,
    val password: String,
    val fullName: String,
    val username: String,
    val imageURL: String? = null,
    val biography: String? = null,
    val location: String? = null,
    val website: String? = null,
    val registeredAt: String? = null,
    val eliminated: Boolean = false,
    val eliminatedAt: String? = null,
    val userType: String = "USER",
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val fcmToken: String
)
