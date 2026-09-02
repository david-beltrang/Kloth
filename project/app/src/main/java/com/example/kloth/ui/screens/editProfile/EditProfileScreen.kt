package com.example.kloth.ui.screens.editProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kloth.ui.screens.editProfile.components.EditProfileTopBar

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit = {},
    viewModel: EditProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            EditProfileTopBar(onCancelClick = onCancelClick)
            EditProfileScreenContent(
                username = uiState.username,
                onUsernameChange = { viewModel.updateUsername(it) },
                bio = uiState.bio,
                onBioChange = { viewModel.updateBio(it) },
                email = uiState.email,
                onEmailChange = { viewModel.updateEmail(it) },
                location = uiState.location,
                onLocationChange = { viewModel.updateLocation(it) },
                website = uiState.website,
                onWebsiteChange = { viewModel.updateWebsite(it) },
                onSaveClick = { /* Implementar save logic */ },
                onDeleteAccount = { /* Implementar delete logic */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
