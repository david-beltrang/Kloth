package com.example.kloth.ui.screens.register

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val navigate: Boolean = false,
    val mostrarMensaje: Boolean = false,
    val errorMessage: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false
)
