package com.example.kloth.ui.screens.forgotPassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordStep
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordScreenContent
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordState
import com.example.kloth.ui.theme.KlothTheme

@Preview(showBackground = true, name = "ForgotPassword - Light Mode")
@Composable
fun ForgotPasswordScreenPreview() {
    KlothTheme(darkTheme = false) {
        ForgotPasswordScreenContent(
            state = ForgotPasswordState(), onEmailChange = {}, onCodeChange = {},
            onNewPasswordChange = {}, onConfirmPasswordChange = {}, onNewPasswordToggle = {},
            onConfirmPasswordToggle = {}, onPrimaryClick = {}, onResendClick = {}, onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "ForgotPassword - Dark Mode")
@Composable
fun ForgotPasswordScreenPreviewDark() {
    KlothTheme(darkTheme = true) {
        ForgotPasswordScreenContent(
            state = ForgotPasswordState(step = ForgotPasswordStep.CodeAndPassword),
            onEmailChange = {}, onCodeChange = {}, onNewPasswordChange = {},
            onConfirmPasswordChange = {}, onNewPasswordToggle = {}, onConfirmPasswordToggle = {},
            onPrimaryClick = {}, onResendClick = {}, onBackClick = {}
        )
    }
}
