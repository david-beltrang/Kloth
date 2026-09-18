package com.example.kloth.ui.screens.register

import androidx.annotation.StringRes
import com.example.kloth.R

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val navigate: Boolean = false,
    val showMessage: Boolean = false,
    val errorMessage: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false
)
