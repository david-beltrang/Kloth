package com.example.kloth.ui.screens.editProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kloth.ui.screens.editProfile.components.EditProfileTopBar

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        EditProfileTopBar(
            onCancelClick = onCancelClick,
            onSaveClick = onSaveClick,
        )
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
            profileImageUrl = uiState.profileImageUrl,
            isLoading = uiState.isLoading,
            onImagePicked = { viewModel.onImagePicked(it) },
            onDeleteAccount = { /* Implementar delete logic */ },
            modifier = Modifier.weight(1f),
        )
    }
}
