package com.example.kloth.ui.screens.forgotPassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun ForgotPasswordScreen(
    forgotPasswordViewModel: ForgotPasswordViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val state by forgotPasswordViewModel.uiState.collectAsState()

    ForgotPasswordScreenContent(
        modifier = modifier,

        // Variables de estado
        email = state.email,

        // Métodos del ViewModel para manejar el estado
        onEmailChange = { forgotPasswordViewModel.onEmailChange(it) },
        onSendClick = { forgotPasswordViewModel.onSendClick() },

        // Navegación
        onBackClick = onBackClick
    )
}
