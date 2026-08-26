package com.example.kloth.ui.screens.editProfile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    KlothTheme(darkTheme = false) {
        EditProfileScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditProfileScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        EditProfileScreen()
    }
}
