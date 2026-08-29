package com.example.kloth.ui.screens.explore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {},
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        ExploreScreenContent(
            modifier = Modifier.fillMaxSize(),
            selectedCategoryIndex = selectedCategoryIndex,
            onCategorySelected = { selectedCategoryIndex = it },
            onProductClick = onProductClick,
        )
    }
}
