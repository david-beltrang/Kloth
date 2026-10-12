package com.example.kloth.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Login Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    KlothTheme(darkTheme = false) {
        LoginScreen(loginViewModel = viewModel())
    }
}

@Preview(
    name = "Login Screen - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF020202
)
@Composable
fun LoginScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        LoginScreen(loginViewModel = viewModel())
    }
}
