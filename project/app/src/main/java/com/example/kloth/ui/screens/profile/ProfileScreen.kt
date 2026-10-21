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
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // Se recarga cada vez que se muestra la pantalla (ej. al volver tras seguir a alguien)
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
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
            selectedTabIndex = uiState.selectedTabIndex,
            userArticles = uiState.userArticles,
            reviewedArticles = uiState.reviewedArticles,
            onTabSelected = { viewModel.onTabSelected(it) },
            onEditProfileClick = onEditProfileClick,
            onLogoutClick = { viewModel.logOutButtonPressed() },
            onProductClick = onProductClick,
            onDeleteArticleClick = onDeleteArticleClick,
            onEditArticleClick = onEditArticleClick,
            followersCount = uiState.followersCount,
            followingCount = uiState.followingCount,
            modifier = Modifier.fillMaxSize()
        )
    }
}
