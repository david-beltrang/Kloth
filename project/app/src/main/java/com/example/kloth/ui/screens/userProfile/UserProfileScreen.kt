package com.example.kloth.ui.screens.userProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.profile.ProfileScreenContent

@Composable
fun UserProfileScreen(
    userId: String,
    userProfileViewModel: UserProfileViewModel,
    modifier: Modifier = Modifier,
    showEdit: Boolean = false,
    onBackClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {}
) {
    val state by userProfileViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        userProfileViewModel.getUserProfile(userId)
        userProfileViewModel.getUserArticles(userId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // No es una pestaña, así que necesita su propio botón para volver
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            state.userNotFound || state.user == null -> UserNotFoundContent()

            else -> {
                val user = state.user!!
                ProfileScreenContent(
                    email = user.email,
                    username = user.username,
                    bio = user.bio,
                    location = user.location,
                    website = user.website,
                    profileImageUrl = user.profileImageUrl,
                    selectedTabIndex = state.selectedTabIndex,
                    userArticles = state.userArticles,
                    reviewedArticles = emptyList(),
                    onTabSelected = { userProfileViewModel.onTabSelected(it) },
                    onProductClick = onProductClick,
                    // Desde esta ruta solo se navega con showEdit = false, por eso no se conectan las acciones de edición
                    onEditProfileClick = {},
                    onDeleteArticleClick = {},
                    onEditArticleClick = {},
                    onLogoutClick = {},
                    showEdit = showEdit,
                    followersCount = user.followers,
                    followingCount = user.following,
                    followed = user.followed,
                    // Uno no se puede seguir a sí mismo
                    showFollowButton = userId != state.currentUserId,
                    onFollowClick = { userProfileViewModel.followOrUnfollowUser(userId) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// estado de error cuando el id no corresponde a ningún usuario
@Composable
private fun UserNotFoundContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.PersonOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.user_profile_not_found_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.user_profile_not_found_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
