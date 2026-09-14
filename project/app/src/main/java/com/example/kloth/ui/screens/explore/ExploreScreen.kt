package com.example.kloth.ui.screens.explore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun ExploreScreen(
    exploreViewModel: ExploreViewModel,
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {},
) {
    val state by exploreViewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        ExploreScreenContent(
            modifier = Modifier.fillMaxSize(),

            // Variables de estado
            products = state.products,
            selectedCategoryIndex = state.selectedCategoryIndex,

            // Métodos del ViewModel para manejar el estado
            onCategorySelected = { exploreViewModel.onCategorySelected(it) },

            // Navegación
            onProductClick = onProductClick,
        )
    }
}
