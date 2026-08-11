package com.example.kloth.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.components.BottomBar

@Composable
fun ItemDetailScreen(
    productId: String,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            Column {
                Divider(
                    color = colorResource(id = R.color.outline_variant),
                    thickness = 1.dp
                )
                BottomBar()
            }
        },
        containerColor = colorResource(id = R.color.surface)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            item {
                HeroImageSection(
                    onBackClick = onBackClick
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
