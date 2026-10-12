package com.example.kloth.ui.screens.createReview

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CreateReviewScreen(
    productId: String,
    reviewId: String? = null,
    onBackClick: () -> Unit,
    onReviewSubmitted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Carga de datos al entrar en la composición usando el ID recibido
    LaunchedEffect(productId) {
        viewModel.loadProductData(productId)
    }

    // Si llega un reviewId, es una edición: precarga los datos existentes
    LaunchedEffect(reviewId) {
        reviewId?.let { viewModel.loadExistingReview(it) }
    }

    CreateReviewScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSubmitClick = { viewModel.submitReview(onReviewSubmitted) },
        onRatingSelected = { viewModel.onRatingSelected(it) },
        onReviewTextChanged = { viewModel.onReviewTextChanged(it) },
        modifier = modifier
    )
}
