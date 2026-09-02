package com.example.kloth.ui.screens.createReview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateReviewScreen(
    onBackClick: () -> Unit,
    onReviewSubmitted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReviewViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateReviewScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSubmitClick = { viewModel.submitReview(onSuccess = onReviewSubmitted) },
        onRatingSelected = viewModel::onRatingSelected,
        onReviewTextChanged = viewModel::onReviewTextChanged,
        modifier = modifier
    )
}