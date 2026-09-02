package com.example.kloth.ui.screens.createReview

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateReviewScreen(
    productId: String,
    onBackClick: () -> Unit,
    onReviewSubmitted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReviewViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Carga de datos al entrar en la composición usando el ID recibido
    LaunchedEffect(productId) {
        viewModel.loadProductData(productId)
    }

    CreateReviewScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSubmitClick = { viewModel.submitReview(onSuccess = onReviewSubmitted) },
        onRatingSelected = { viewModel.onRatingSelected(it) },
        onReviewTextChanged = { viewModel.onReviewTextChanged(it) },
        modifier = modifier
    )
}
