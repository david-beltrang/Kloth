package com.example.kloth.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Profile Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenPreview() {
    KlothTheme(darkTheme = false) {
        ProfileScreen()
    }
}

@Preview(
    name = "Profile Screen - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        ProfileScreen()
    }
}
