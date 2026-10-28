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
import androidx.compose.ui.unit.dp
import com.example.kloth.data.local.UserProfileInfo
import com.example.kloth.ui.screens.followList.components.FollowUserRow

@Composable
fun FollowListScreenContent(
    users: List<UserProfileInfo>,
    isLoading: Boolean,
    listType: String,
    listOwnerId: String = "",
    currentUserId: String? = null,
    followingUserIds: Set<String> = emptySet(),
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    onUserAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = if (listType == "followers") "Seguidores" else "Seguidos",
                style = MaterialTheme.typography.titleLarge
            )
        }
        when {
            isLoading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally).padding(24.dp))
            users.isEmpty() -> Text(
                text = if (listType == "followers") "Aún no tiene seguidores" else "Aún no sigue a usuarios",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            else -> LazyColumn {
                items(users, key = { it.id }) { user ->
                    val isOwnList = currentUserId != null && currentUserId == listOwnerId
                    val actionLabel = when {
                        currentUserId == null || currentUserId == user.id -> null
                        isOwnList && listType == "followers" -> "Eliminar"
                        user.id in followingUserIds -> "Dejar de seguir"
                        else -> "Seguir"
                    }
                    FollowUserRow(
                        user = user,
                        actionLabel = actionLabel,
                        onClick = { onUserClick(user.id) },
                        onActionClick = { onUserAction(user.id) }
                    )
                }
            }
        }
    }
}
