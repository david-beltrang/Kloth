package com.example.kloth.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProfileScreenContent(
        onEditProfileClick = onEditProfileClick,
        modifier = modifier
    )
}
