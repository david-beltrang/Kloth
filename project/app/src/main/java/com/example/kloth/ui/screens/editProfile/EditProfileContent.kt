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
    username: String,
    onUsernameChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    website: String,
    onWebsiteChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            onUsernameChange = onUsernameChange,
            bio = bio,
            onBioChange = onBioChange,
            email = email,
            onEmailChange = onEmailChange,
            location = location,
            onLocationChange = onLocationChange,
            website = website,
            onWebsiteChange = onWebsiteChange,
            onSaveClick = onSaveClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        DangerZoneSection(onDeleteAccount = onDeleteAccount)
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
