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
    KlothTheme {
        ProfileScreen()
    }
}
