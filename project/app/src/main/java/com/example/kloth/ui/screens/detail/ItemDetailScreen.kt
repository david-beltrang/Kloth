package com.example.kloth.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ItemDetailScreen(
    productId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ItemDetailScreenContent(
        productId = productId,
        navController = navController,
        modifier = modifier
    )
}
