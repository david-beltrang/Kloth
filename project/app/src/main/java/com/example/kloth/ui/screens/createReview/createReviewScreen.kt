package com.example.kloth.ui.screens.createReview

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CreateReviewScreen(
    productId: String,
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

    // Cuando la reseña se guarda, se vuelve al detalle
    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            onReviewSubmitted()
            viewModel.onSubmittedConsumed()
        }
    }

    CreateReviewScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSubmitClick = { viewModel.submitReview() },
        onRatingSelected = { viewModel.onRatingSelected(it) },
        onReviewTextChanged = { viewModel.onReviewTextChanged(it) },
        modifier = modifier
    )
}
