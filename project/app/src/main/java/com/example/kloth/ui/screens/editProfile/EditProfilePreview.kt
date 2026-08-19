package com.example.kloth.ui.screens.editProfile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kloth.ui.theme.KlothTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    KlothTheme {
        EditProfileScreenContent(navController = rememberNavController())
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditProfileScreenDarkPreview() {
    KlothTheme {
        EditProfileScreenContent(navController = rememberNavController())
    }
}
