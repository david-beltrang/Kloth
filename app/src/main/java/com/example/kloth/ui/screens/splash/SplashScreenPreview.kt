package com.example.kloth.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Splash Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SplashScreenPreview() {
    KlothTheme(darkTheme = false) {
        SplashScreenContent(isLoading = true)
    }
}

@Preview(
    name = "Splash Screen - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF020202
)
@Composable
fun SplashScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        SplashScreenContent(isLoading = true)
    }
}
