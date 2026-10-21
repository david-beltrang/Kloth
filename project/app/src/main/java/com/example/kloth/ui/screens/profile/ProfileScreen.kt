package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kloth.data.local.PostItem

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onDeleteArticleClick: (PostItem) -> Unit = {},
    onEditArticleClick: (String) -> Unit = {},
    profileUserId: String? = null,
    onFollowListClick: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // Se recarga cada vez que la pantalla vuelve a entrar en composición
    LaunchedEffect(Unit) {
        viewModel.refreshProfile(profileUserId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProfileScreenContent(
            email = uiState.email,
            username = uiState.username,
            bio = uiState.bio,
            location = uiState.location,
            website = uiState.website,
            profileImageUrl = uiState.profileImageUrl,
            viewingOwnProfile = uiState.viewingOwnProfile,
            followersCount = uiState.followersCount,
            followingCount = uiState.followingCount,
            isFollowing = uiState.isFollowing,
            isFollowActionInProgress = uiState.isFollowActionInProgress,
            selectedTabIndex = uiState.selectedTabIndex,
            userArticles = uiState.userArticles,
            reviewedArticles = uiState.reviewedArticles,
            onTabSelected = { viewModel.onTabSelected(it) },
            onEditProfileClick = onEditProfileClick,
            onFollowClick = { viewModel.toggleFollow() },
            onLogoutClick = { viewModel.logOutButtonPressed() },
            onFollowersClick = { uiState.targetUserId?.let { onFollowListClick(it, "followers") } },
            onFollowingClick = { uiState.targetUserId?.let { onFollowListClick(it, "following") } },
            onProductClick = onProductClick,
            onDeleteArticleClick = onDeleteArticleClick,
            onEditArticleClick = onEditArticleClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}
