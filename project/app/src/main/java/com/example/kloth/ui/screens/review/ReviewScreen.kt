package com.example.kloth.ui.screens.review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    reviewViewModel: ReviewViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by reviewViewModel.uiState.collectAsState()

    ReviewScreenContent(
        modifier = modifier,
        resena = state.resena,
        nuevoComentarioTexto = state.nuevoComentarioTexto,
        ordenMasRecientes = state.ordenMasRecientes,
        yaComento = state.yaComento,
        onTextoComentarioChange = { reviewViewModel.onTextoComentarioChange(it) },
        onEnviarComentario = { reviewViewModel.onEnviarComentario() },
        onLikeResena = { reviewViewModel.onLikeResena() },
        onLikeComentario = { reviewViewModel.onLikeComentario(it) },
        onEliminarComentario = { reviewViewModel.onEliminarComentario(it) },
        onToggleOrden = { reviewViewModel.onToggleOrden() },
        onBackClick = onBackClick
    )
}