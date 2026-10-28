package com.example.kloth.ui.screens.followList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.data.local.UserProfileInfo
import com.example.kloth.ui.screens.followList.components.FollowUserRow

@Composable
fun FollowListScreenContent(
    users: List<UserProfileInfo>,
    isLoading: Boolean,
    listType: String,
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    onUnfollowClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.follow_list_back))
            }
            Text(
                text = stringResource(if (listType == "followers") R.string.follow_list_followers_title else R.string.follow_list_following_title),
                style = MaterialTheme.typography.titleLarge
            )
        }
        when {
            isLoading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally).padding(24.dp))
            users.isEmpty() -> Text(
                text = stringResource(if (listType == "followers") R.string.follow_list_empty_followers else R.string.follow_list_empty_following),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            else -> LazyColumn {
                items(users, key = { it.id }) { user ->
                    FollowUserRow(
                        user = user,
                        showUnfollowButton = listType == "following",
                        onClick = { onUserClick(user.id) },
                        onUnfollowClick = { onUnfollowClick(user.id) }
                    )
                }
            }
        }
    }
}
