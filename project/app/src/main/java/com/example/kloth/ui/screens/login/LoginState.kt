package com.example.kloth.ui.screens.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val navigate: Boolean = false,
    val errorMessage: String = "",
    val showMessage: Boolean = false
)
