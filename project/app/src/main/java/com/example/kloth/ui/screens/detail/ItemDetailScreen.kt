package com.example.kloth.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kloth.R

@Composable
fun ItemDetailScreen(
    productId: String,
    detailViewModel: DetailViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onAddToCartClick: () -> Unit = {},
    onWriteReviewClick: () -> Unit = {}
) {
    // Escuchar el estado del ViewModel
    val state by detailViewModel.uiState.collectAsState()

    // Por ahra usamos el LaunchedEffect porque la vista depende de un ID
    LaunchedEffect(productId) {
        detailViewModel.loadProduct(productId)
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.product == null) {
        ProductNotFoundContent(
            onBackClick = onBackClick,
            modifier = modifier
        )
        return
    }

    ItemDetailScreenContent(
        // Variables de estado (Datos)
        product = state.product!!,

        // Métodos para manejar el estado (Eventos)
        onFavoriteClick = { detailViewModel.toggleFavorite() },

        // Navegación y otros eventos
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onAddToCartClick = onAddToCartClick,
        onWriteReviewClick = onWriteReviewClick,
        onLikeReviewClick = { reviewId -> /* Accion like resena */ },
        onCommentReviewClick = { reviewId -> /* Accion comentar resena */ },
        modifier = modifier
    )
}

/**
 * Estado de error mostrado cuando el identificador recibido no corresponde
 * a ningun articulo del catalogo.
 */
@Composable
private fun ProductNotFoundContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.detail_not_found_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.detail_not_found_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = onBackClick) {
                Text(text = stringResource(R.string.detail_not_found_back))
            }
        }
    }
}
