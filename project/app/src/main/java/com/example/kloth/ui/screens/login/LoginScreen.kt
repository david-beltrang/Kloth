package com.example.kloth.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val state by loginViewModel.uiState.collectAsState()

    LoginScreenContent(
        modifier = modifier,
        email = state.email,
        password = state.password,
        isPasswordVisible = state.isPasswordVisible,
        errorMessage = if (state.showMessage) state.errorMessage else "",
        onEmailChange = { loginViewModel.onEmailChange(it) },
        onPasswordChange = { loginViewModel.onPasswordChange(it) },
        onPasswordToggleClick = { loginViewModel.togglePasswordVisibility() },
        onLoginClick = { loginViewModel.onLoginClick() },
        onForgotPasswordClick = onForgotPasswordClick,
        onRegisterClick = onRegisterClick
    )
}
