package com.example.kloth.ui.screens.explore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.kloth.ui.components.ErrorState

@Composable
fun ExploreScreen(
    exploreViewModel: ExploreViewModel,
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {},
) {
    val state by exploreViewModel.uiState.collectAsState()

    // La pantalla es la que dispara la carga: cada vez que se entra (o se vuelve
    // de una pantalla encima) se refresca para mostrar datos actualizados.
    LaunchedEffect(Unit) {
        exploreViewModel.loadProducts()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.errorMessage != null -> {
                ErrorState(
                    message = state.errorMessage.orEmpty(),
                    onRetry = { exploreViewModel.retry() },
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
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
    }
}
