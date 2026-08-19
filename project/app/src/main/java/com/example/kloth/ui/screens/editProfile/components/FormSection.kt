package com.example.kloth.ui.screens.editProfile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.DividerColor
import com.example.kloth.ui.theme.TextGray

@Composable
fun FormSection(
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
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit = {}
) {
    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        EditProfileTextField(
            label = stringResource(R.string.edit_profile_label_username),
            value = username,
            onValueChange = onUsernameChange,
            leadingIcon = Icons.Outlined.Search
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditProfileTextField(
            label = stringResource(R.string.edit_profile_label_bio),
            value = bio,
            onValueChange = onBioChange,
            placeholder = stringResource(R.string.edit_profile_placeholder_bio),
            singleLine = false,
            height = 100,
            showCharacterCount = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditProfileTextField(
            label = stringResource(R.string.edit_profile_label_email),
            value = email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.edit_profile_placeholder_email),
            leadingIcon = Icons.Outlined.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditProfileTextField(
            label = stringResource(R.string.edit_profile_label_location),
            value = location,
            onValueChange = onLocationChange,
            placeholder = stringResource(R.string.edit_profile_placeholder_location),
            leadingIcon = Icons.Outlined.LocationOn
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditProfileTextField(
            label = stringResource(R.string.edit_profile_label_website),
            value = website,
            onValueChange = onWebsiteChange,
            placeholder = stringResource(R.string.edit_profile_placeholder_website),
            leadingIcon = Icons.Outlined.Public
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DividerColor,
                contentColor = TextGray
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = true // Enabled for interactivity as per requirements
        ) {
            Text(
                text = stringResource(R.string.edit_profile_btn_save),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormSectionPreview() {
    FormSection(
        username = "Username",
        onUsernameChange = {},
        bio = "",
        onBioChange = {},
        email = "",
        onEmailChange = {},
        location = "",
        onLocationChange = {},
        website = "",
        onWebsiteChange = {}
    )
}
