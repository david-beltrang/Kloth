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

        // Variables de estado
        email = state.email,
        password = state.password,
        isPasswordVisible = state.isPasswordVisible,

        // Métodos del View Model para manejar el estado
        onEmailChange = { loginViewModel.onEmailChange(it) },
        onPasswordChange = { loginViewModel.onPasswordChange(it) },
        onPasswordToggleClick = { loginViewModel.togglePasswordVisibility() },
        onLoginClick = { loginViewModel.onLoginClick() },

        // Navegación (estos vienen desde AppNavigation)
        onForgotPasswordClick = onForgotPasswordClick,
        onRegisterClick = onRegisterClick
    )
}
