package com.example.kloth.ui.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.kloth.ui.screens.feed.components.FeedTabRow

@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel,
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {}
) {
    // Escuchar el estado del ViewModel
    val state by feedViewModel.uiState.collectAsState()

    // La pantalla es la que dispara la carga: cada vez que se entra (o se vuelve
    // de una pantalla encima) se refresca para mostrar datos actualizados.
    LaunchedEffect(Unit) {
        feedViewModel.getAllPosts()
    }
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FeedTabRow(
                selectedTabIndex = state.selectedTabIndex,
                onTabSelected = { feedViewModel.onTabSelected(it) }
            )
            FeedScreenContent(
                // Variables de estado (Datos)
                posts = state.posts,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,

                // Eventos
                onRetry = { feedViewModel.getAllPosts() },

                // Navegación
                onProductClick = onProductClick
            )
        }
    }
}
