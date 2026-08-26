package com.example.kloth.ui.screens.editProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.kloth.R
import com.example.kloth.ui.screens.editProfile.components.EditProfileTopBar

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit = {}
) {
    val defaultUsername = stringResource(R.string.edit_profile_mock_username)
    var username by remember { mutableStateOf(defaultUsername) }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            EditProfileTopBar(onCancelClick = onCancelClick)
            EditProfileScreenContent(
                username = username,
                onUsernameChange = { username = it },
                bio = bio,
                onBioChange = { bio = it },
                email = email,
                onEmailChange = { email = it },
                location = location,
                onLocationChange = { location = it },
                website = website,
                onWebsiteChange = { website = it },
                onSaveClick = { /* TODO: Implement save logic */ },
                onDeleteAccount = { /* TODO: Implement delete logic */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

