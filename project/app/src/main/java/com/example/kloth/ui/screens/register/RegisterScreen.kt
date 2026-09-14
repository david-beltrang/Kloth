package com.example.kloth.ui.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {}
) {
    val state by registerViewModel.uiState.collectAsState()

    RegisterScreenContent(
        modifier = modifier,

        //Variables de estado
        fullName = state.fullName,
        email = state.email,
        password = state.password,
        confirmPassword = state.confirmPassword,
        isPasswordVisible = state.isPasswordVisible,
        isConfirmPasswordVisible = state.isConfirmPasswordVisible,

        //Metodos del View Model para manejar el estado
        onFullNameChange = { registerViewModel.onFullNameChange(it) },
        onEmailChange = { registerViewModel.onEmailChange(it) },
        onPasswordChange = { registerViewModel.onPasswordChange(it) },
        onConfirmPasswordChange = { registerViewModel.onConfirmPasswordChange(it) },
        onPasswordToggleClick = { registerViewModel.togglePasswordVisibility() },
        onConfirmPasswordToggleClick = { registerViewModel.toggleConfirmPasswordVisibility() },

        //Navegacion
        onRegisterClick = { registerViewModel.registerButtonPressed() },
        onLoginClick = onLoginClick
    )
}
