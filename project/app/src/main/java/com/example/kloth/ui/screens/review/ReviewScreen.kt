package com.example.kloth.ui.screens.review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReviewScreen(
    reviewId: String? = null,
    modifier: Modifier = Modifier,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onEditClick: (String, String) -> Unit = { _, _ -> },
    onProfileClick: (Int) -> Unit = {}
) {
    val state by reviewViewModel.uiState.collectAsState()

    LaunchedEffect(reviewId) {
        reviewId?.let { reviewViewModel.loadReview(it) }
    }

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    ReviewScreenContent(
        modifier = modifier,
        resena = state.resena,
        nuevoComentarioTexto = state.nuevoComentarioTexto,
        ordenMasRecientes = state.ordenMasRecientes,
        yaComento = state.yaComento,
        isSending = state.isSending,
        errorMessage = state.errorMessage,
        onTextoComentarioChange = { reviewViewModel.onTextoComentarioChange(it) },
        onEnviarComentario = { reviewViewModel.onEnviarComentario() },
        onLikeResena = { reviewViewModel.onLikeResena() },
        onLikeComentario = { reviewViewModel.onLikeComentario(it) },
        onEliminarComentario = { reviewViewModel.onEliminarComentario(it) },
        onToggleOrden = { reviewViewModel.onToggleOrden() },
        onBackClick = onBackClick,
        onEditClick = { onEditClick(state.resena.productId, state.resena.id) },
        onDeleteClick = { reviewViewModel.onDeleteReview(onBackClick) },
        onProfileClick = onProfileClick
    )
}
