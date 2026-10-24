package com.example.kloth.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.utils.ProfileAsyncImage

@Composable
fun ProfileHeaderSection(
    email: String,
    username: String?,
    bio: String?,
    location: String?,
    website: String?,
    profileImageUrl: String?,
    publicationsCount: Int = 0,
    followersCount: Int = 0,
    followingCount: Int = 0,
    reviewsCount: Int = 0,
    viewingOwnProfile: Boolean = true,
    isFollowing: Boolean = false,
    isFollowActionInProgress: Boolean = false,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onFollowClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {}
) {
    val displayName = if (!username.isNullOrBlank()) username else email.substringBefore("@").replaceFirstChar { it.uppercase() }
    val displayBio = if (!bio.isNullOrBlank()) bio else "Mejora tu biografía para que otros te conozcan"
    val displayLocation = if (!location.isNullOrBlank()) location else "Añade tu ubicación"
    val displayWebsite = if (!website.isNullOrBlank()) website else "Añade un sitio web"

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ProfileAsyncImage(
            profileImage = profileImageUrl ?: "",
            size = 100,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape
                )
                .padding(3.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = email,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = displayBio,
            style = MaterialTheme.typography.bodyMedium,
            color = if (bio.isNullOrBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .then(if (viewingOwnProfile) Modifier.clickable { onEditProfileClick() } else Modifier)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = if (viewingOwnProfile) Modifier.clickable { onEditProfileClick() } else Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = displayLocation,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (location.isNullOrBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = if (viewingOwnProfile) Modifier.clickable { onEditProfileClick() } else Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = displayWebsite,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (website.isNullOrBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStatItem(
                value = publicationsCount.toString(),
                label = stringResource(R.string.profile_publications)
            )
            ProfileStatItem(
                value = followersCount.toString(),
                label = stringResource(R.string.profile_followers),
                enabled = true,
                onClick = onFollowersClick
            )
            ProfileStatItem(
                value = followingCount.toString(),
                label = stringResource(R.string.profile_following),
                enabled = true,
                onClick = onFollowingClick
            )
            ProfileStatItem(
                value = reviewsCount.toString(),
                label = stringResource(R.string.profile_reviews)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!viewingOwnProfile) Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(
                    if (isFollowing) MaterialTheme.colorScheme.surfaceContainerHighest
                    else androidx.compose.ui.graphics.Color.Black
                )
                .then(
                    if (isFollowActionInProgress) Modifier
                    else Modifier.clickable { onFollowClick() }
                )
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(if (isFollowing) R.string.profile_unfollow else R.string.profile_follow),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = if (isFollowing) MaterialTheme.colorScheme.onSurfaceVariant
                else androidx.compose.ui.graphics.Color.White
            )
        }

        // RF-05: modificar perfil
        if (viewingOwnProfile) Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onEditProfileClick() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_edit),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Logout Button
        if (viewingOwnProfile) Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(999.dp))
                .border(1.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(999.dp))
                .clickable { onLogoutClick() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.profile_logout),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ProfileStatItem(
    value: String,
    label: String,
    enabled: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderSectionPreview() {
    KlothTheme(darkTheme = false) {
        ProfileHeaderSection(
            email = "test@example.com",
            username = null,
            bio = null,
            location = null,
            website = null,
            profileImageUrl = null,
            publicationsCount = 5,
            followersCount = 120,
            followingCount = 80,
            reviewsCount = 3
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileHeaderSectionDarkPreview() {
    KlothTheme(darkTheme = true) {
        ProfileHeaderSection(
            email = "test@example.com",
            username = null,
            bio = null,
            location = null,
            website = null,
            profileImageUrl = null,
            publicationsCount = 5,
            followersCount = 120,
            followingCount = 80,
            reviewsCount = 3
        )
    }
}
