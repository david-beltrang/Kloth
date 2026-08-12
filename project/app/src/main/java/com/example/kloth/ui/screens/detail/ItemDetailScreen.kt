package com.example.kloth.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.components.BottomBar
import com.example.kloth.ui.components.feedComponents.KlothBottomNavigation

@Composable
fun ItemDetailScreen(
    productId: String
) {
    Scaffold(
        bottomBar = {
            Column {
                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
                KlothBottomNavigation()
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            item {
                HeroImageSection(
                    onBackClick = { /* Navegación desactivada */ }
                )
            }
            item {
                GeneralInfoSection(
                    productId = productId
                )
            }
            item {
                RatingsAndReviewsSection()
            }
        }
    }
}
