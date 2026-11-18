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
        state = state,
        onEmailChange = forgotPasswordViewModel::onEmailChange,
        onCodeChange = forgotPasswordViewModel::onCodeChange,
        onNewPasswordChange = forgotPasswordViewModel::onNewPasswordChange,
        onConfirmPasswordChange = forgotPasswordViewModel::onConfirmPasswordChange,
        onNewPasswordToggle = forgotPasswordViewModel::toggleNewPasswordVisibility,
        onConfirmPasswordToggle = forgotPasswordViewModel::toggleConfirmPasswordVisibility,
        onPrimaryClick = {
            if (state.step == ForgotPasswordStep.Email) forgotPasswordViewModel.onSendClick()
            else forgotPasswordViewModel.onResetClick()
        },
        onResendClick = forgotPasswordViewModel::onResendClick,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
