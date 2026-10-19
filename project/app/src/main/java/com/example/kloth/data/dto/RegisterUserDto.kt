package com.example.kloth.data.dto

data class RegisterUserDto(
    val fullName: String,
    val imageURL: String? = null,
    val biography: String? = null,
    val location: String? = null,
    val website: String? = null
)
