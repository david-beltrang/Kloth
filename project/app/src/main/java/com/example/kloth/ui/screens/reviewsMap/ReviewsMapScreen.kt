package com.example.kloth.ui.screens.reviewsMap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReviewsMapScreen(
    onBackClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewsMapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Lectura única al entrar en la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadRecentReviews()
    }

    ReviewsMapScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetryClick = { viewModel.loadRecentReviews() },
        onRefreshClick = { viewModel.loadRecentReviews() },
        onPinSelected = { viewModel.onPinSelected(it) },
        onPinDismissed = { viewModel.onPinDismissed() },
        onReviewClick = onReviewClick,
        modifier = modifier
    )
}
