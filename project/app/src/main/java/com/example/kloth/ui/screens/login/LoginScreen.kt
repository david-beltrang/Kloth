package com.example.kloth.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val state by loginViewModel.uiState.collectAsState()
    
    // Obtenemos el mensaje si hay un error
    val errorMessage = if (state.showMessage && state.errorMessageRes != 0) {
        stringResource(state.errorMessageRes)
    } else {
        ""
    }

    LoginScreenContent(
        modifier = modifier,

        // Variables de estado
        email = state.email,
        password = state.password,
        isPasswordVisible = state.isPasswordVisible,
        errorMessage = errorMessage,

        // Métodos del View Model para manejar el estado
        onEmailChange = { loginViewModel.onEmailChange(it) },
        onPasswordChange = { loginViewModel.onPasswordChange(it) },
        onPasswordToggleClick = { loginViewModel.togglePasswordVisibility() },
        onLoginClick = { loginViewModel.onLoginClick() },

        // Navegación
        onForgotPasswordClick = onForgotPasswordClick,
        onRegisterClick = onRegisterClick
    )
}
