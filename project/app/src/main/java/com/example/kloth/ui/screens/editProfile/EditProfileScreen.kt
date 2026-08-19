package com.example.kloth.ui.screens.editProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kloth.ui.screens.editProfile.components.EditProfileTopBar

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        EditProfileTopBar()
        EditProfileScreenContent(
            modifier = Modifier.weight(1f)
        )
    }
}

