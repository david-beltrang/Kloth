package com.example.kloth.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Login Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    KlothTheme {
        LoginScreen()
    }
}
