package com.example.kloth.ui.screens.forgotPassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(showBackground = true, name = "ForgotPassword - Light Mode")
@Composable
fun ForgotPasswordScreenPreview() {
    KlothTheme(darkTheme = false) {
        ForgotPasswordScreen()
    }
}

@Preview(showBackground = true, name = "ForgotPassword - Dark Mode")
@Composable
fun ForgotPasswordScreenPreviewDark() {
    KlothTheme(darkTheme = true) {
        ForgotPasswordScreen()
    }
}
