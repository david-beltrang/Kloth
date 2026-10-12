package com.example.kloth.ui.screens.editProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.screens.editProfile.components.EditProfileTopBar
import com.example.kloth.ui.theme.KlothTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    KlothTheme(darkTheme = false) {
        Column(modifier = Modifier.fillMaxSize()) {
            EditProfileTopBar()
            EditProfileScreenContent(
                username = "johndoe",
                onUsernameChange = {},
                bio = "Amante de la moda sostenible",
                onBioChange = {},
                email = "john@example.com",
                onEmailChange = {},
                location = "Madrid, España",
                onLocationChange = {},
                website = "https://kloth.example.com",
                onWebsiteChange = {},
                profileImageUrl = null,
                isLoading = false,
                onImagePicked = {},
                onDeleteAccount = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditProfileScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        Column(modifier = Modifier.fillMaxSize()) {
            EditProfileTopBar()
            EditProfileScreenContent(
                username = "johndoe",
                onUsernameChange = {},
                bio = "Amante de la moda sostenible",
                onBioChange = {},
                email = "john@example.com",
                onEmailChange = {},
                location = "Madrid, España",
                onLocationChange = {},
                website = "https://kloth.example.com",
                onWebsiteChange = {},
                profileImageUrl = null,
                isLoading = false,
                onImagePicked = {},
                onDeleteAccount = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}
