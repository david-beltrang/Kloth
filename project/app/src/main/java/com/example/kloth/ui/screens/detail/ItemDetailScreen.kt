package com.example.kloth.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ItemDetailScreen(
    productId: String,
    modifier: Modifier = Modifier
) {
    ItemDetailScreenContent(
        productId = productId,
        modifier = modifier
    )
}
