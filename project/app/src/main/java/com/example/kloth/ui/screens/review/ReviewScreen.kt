package com.example.kloth.ui.screens.review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onUserClick: (String) -> Unit = {}
) {
    val state by reviewViewModel.uiState.collectAsState()

    LaunchedEffect(state.reviewDeleted) {
        if (state.reviewDeleted) onBackClick()
    }

    ReviewScreenContent(
        modifier = modifier,
        resena = state.resena,
        isOwnReview = state.resena.authorId.isNotBlank() && state.resena.authorId == state.currentUserId,
        isDeletingReview = state.isDeletingReview,
        deleteReviewError = state.deleteReviewError,
        currentUserImageUrl = state.currentUserImageUrl,
        nuevoComentarioTexto = state.nuevoComentarioTexto,
        ordenMasRecientes = state.ordenMasRecientes,
        yaComento = state.yaComento,
        onTextoComentarioChange = { reviewViewModel.onTextoComentarioChange(it) },
        onEnviarComentario = { reviewViewModel.onEnviarComentario() },
        onLikeResena = { reviewViewModel.onLikeResena() },
        onDeleteReview = { reviewViewModel.onDeleteReview() },
        onLikeComentario = { reviewViewModel.onLikeComentario(it) },
        onEliminarComentario = { reviewViewModel.onEliminarComentario(it) },
        onToggleOrden = { reviewViewModel.onToggleOrden() },
        onBackClick = onBackClick,
        onUserClick = onUserClick
    )
}
