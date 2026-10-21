package com.example.kloth.ui.screens.userProfile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "User Profile Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun UserProfileScreenPreview() {
    KlothTheme(darkTheme = false) {
        UserProfileScreen(userId = "preview", userProfileViewModel = viewModel())
    }
}

@Preview(
    name = "User Profile Screen - Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun UserProfileScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        UserProfileScreen(userId = "preview", userProfileViewModel = viewModel())
    }
}
