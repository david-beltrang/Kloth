package com.example.kloth.ui.screens.editProfile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.editProfile.components.*

@Composable
fun EditProfileScreenContent(
    modifier: Modifier = Modifier
) {
    val defaultUsername = stringResource(R.string.edit_profile_mock_username)
    var username by remember { mutableStateOf(defaultUsername) }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        ProfileImageEditor(
            onImageChange = { /* TODO */ }
        )

        Spacer(modifier = Modifier.height(32.dp))

        FormSection(
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
            onSaveClick = { /* TODO */ }
        )

        Spacer(modifier = Modifier.height(32.dp))

        DangerZoneSection(onDeleteAccount = { /* TODO */ })
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
