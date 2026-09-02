package com.example.kloth.ui.screens.createReview

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateReviewScreen(
    onBackClick: () -> Unit,
    onReviewSubmitted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReviewViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateReviewScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSubmitClick = { viewModel.submitReview(onSuccess = onReviewSubmitted) },
        onRatingSelected = viewModel::onRatingSelected,
        onReviewTextChanged = viewModel::onReviewTextChanged,
        modifier = modifier
    )
}