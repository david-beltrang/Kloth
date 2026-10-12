package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditProfileClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onCommentReviewClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // La pantalla es la que dispara la carga: cada vez que se entra (o se vuelve
    // de una pantalla encima) se refresca para mostrar datos actualizados.
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProfileScreenContent(
            //metodos de estado
            email = uiState.email,
            profileImageUrl = uiState.profileImageUrl,
            profileUserName = uiState.profileUserName,
            isCurrentUser = uiState.isCurrentUser,
            selectedTabIndex = uiState.selectedTabIndex,
            onTabSelected = { viewModel.onTabSelected(it) },
            //navegacion
            onEditProfileClick = onEditProfileClick,
            onLogoutClick = { viewModel.logOutButtonPressed() },
            onBackClick = onBackClick,
            userArticles = uiState.userArticles,
            userReviews = uiState.userReviews,
            isLoadingArticles = uiState.isLoadingArticles,
            isLoadingReviews = uiState.isLoadingReviews,
            onProductClick = onProductClick,
            onCommentReviewClick = onCommentReviewClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}