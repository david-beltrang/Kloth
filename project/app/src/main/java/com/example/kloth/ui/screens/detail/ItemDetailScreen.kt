package com.example.kloth.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kloth.data.sampleProductDetail

@Composable
fun ItemDetailScreen(
    productId: String,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onAddToCartClick: () -> Unit = {},
    onWriteReviewClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Más adelante aquí se conectará el ViewModel/Repository mediante el productId
    val productData = sampleProductDetail

    ItemDetailScreenContent(
        product = productData,
        onBackClick = onBackClick,
        onFavoriteClick = { /* Toggle favorito */ },
        onShareClick = onShareClick,
        onAddToCartClick = onAddToCartClick,
        onWriteReviewClick = onWriteReviewClick,
        onLikeReviewClick = { reviewId -> /* Acción like reseña */ },
        onCommentReviewClick = { reviewId -> /* Acción comentar reseña */ },
        modifier = modifier
    )
}