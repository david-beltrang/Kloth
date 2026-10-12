package com.example.kloth.ui.screens.forgotPassword

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val navigateBack: Boolean = false,
    val errorMessage: String = "",
    val showMessage: Boolean = false
)
