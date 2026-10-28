package com.example.kloth.ui.screens.followList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.data.local.UserProfileInfo
import com.example.kloth.R
import com.example.kloth.ui.utils.ProfileAsyncImage

@Composable
fun FollowUserRow(
    user: UserProfileInfo,
    actionLabel: String?,
    onClick: () -> Unit,
    onUnfollowClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileAsyncImage(profileImage = user.profileImageUrl.orEmpty(), size = 48)
        Column(Modifier.weight(1f)) {
            Text(user.username?.takeIf { it.isNotBlank() } ?: user.email.substringBefore("@"), style = MaterialTheme.typography.titleMedium)
            if (user.username?.isNotBlank() == true && user.email.isNotBlank()) {
                Text(user.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (showUnfollowButton) {
            TextButton(onClick = onUnfollowClick) { Text(stringResource(R.string.follow_list_unfollow)) }
        } else {
            TextButton(onClick = {}) { Text(stringResource(R.string.follow_list_remove)) }
        }
    }
}

