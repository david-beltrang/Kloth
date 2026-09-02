package com.example.kloth.ui.screens.login

import androidx.annotation.StringRes
import com.example.kloth.R

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val navigate: Boolean = false,
    @StringRes val errorMessageRes: Int = 0,
    val showMessage: Boolean = false
)
