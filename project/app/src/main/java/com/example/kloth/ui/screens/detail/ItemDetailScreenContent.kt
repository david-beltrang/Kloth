package com.example.kloth.ui.screens.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kloth.ui.screens.detail.components.GeneralInfoSection
import com.example.kloth.ui.screens.detail.components.HeroImageSection
import com.example.kloth.ui.screens.detail.components.RatingsAndReviewsSection

@Composable
fun ItemDetailScreenContent(
    productId: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            HeroImageSection(
                onBackClick = { }
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
