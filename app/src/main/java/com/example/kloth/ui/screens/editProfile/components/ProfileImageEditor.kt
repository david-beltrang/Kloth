package com.example.kloth.ui.screens.editProfile.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.utils.ProfileAsyncImage

@Composable
fun ProfileImageEditor(
    profileImageUrl: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onImageChange: (Uri) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(contentAlignment = Alignment.Center) {
                ProfileAsyncImage(
                    profileImage = profileImageUrl ?: "",
                    size = 96,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
                
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            PickImageButton(
                onImagePicked = onImageChange
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.edit_profile_change_photo),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileImageEditorPreview() {
    KlothTheme {
        ProfileImageEditor(profileImageUrl = null, isLoading = false)
    }
}
