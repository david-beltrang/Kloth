package com.example.kloth.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kloth.ui.theme.KlothTheme

@Preview(
    name = "Item Detail Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ItemDetailScreenPreview() {
    KlothTheme {
        ItemDetailScreen(
            productId = "abrigo_negro",
            navController = rememberNavController()
        )
    }
}
