package com.example.kloth.ui.screens.forgotPassword

enum class ForgotPasswordStep { Email, CodeAndPassword }

data class ForgotPasswordState(
    val step: ForgotPasswordStep = ForgotPasswordStep.Email,
    val email: String = "",
    val code: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val resendSeconds: Int = 0,
    val isLoading: Boolean = false,
    val navigateBack: Boolean = false,
    val errorMessage: String = "",
    val showMessage: Boolean = false,
    val messageIsSuccess: Boolean = false
)
