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
                mockPosts = state.posts,

                // Navegación y Eventos
                onProductClick = onProductClick
            )
        }
    }
}
