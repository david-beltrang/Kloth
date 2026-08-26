package com.example.kloth.ui.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Register Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterScreenPreview() {
    KlothTheme(darkTheme = false) {
        RegisterScreen()
    }
}

@Preview(
    name = "Register Screen - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF020202
)
@Composable
fun RegisterScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        RegisterScreen()
    }
}
