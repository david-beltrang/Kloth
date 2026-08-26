package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.data.FakeArticle
import com.example.kloth.ui.screens.detail.components.ProductCatalogCard
import com.example.kloth.ui.screens.feed.components.ProfileTabRow
import com.example.kloth.ui.screens.profile.components.ProfileHeaderSection

@Composable
fun ProfileScreenContent(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 24.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {

        item(span = { GridItemSpan(2) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                ProfileHeaderSection(onEditProfileClick = onEditProfileClick)

                Spacer(modifier = Modifier.height(8.dp))

                ProfileTabRow(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = onTabSelected
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (selectedTabIndex == 0) {
            // Publicaciones (Usando datos de FakeArticle)
            item {
                val p = FakeArticle.detailSuit
                ProductCatalogCard(
                    title = p.title,
                    category = p.categoryTag,
                    rating = p.averageRating.toString(),
                    reviews = p.reviewsCountText.filter { it.isDigit() },
                    imageModel = p.imageUrl ?: p.imageRes,
                    onClick = { }
                )
            }
            item {
                val p = FakeArticle.detailVestido
                ProductCatalogCard(
                    title = p.title,
                    category = p.categoryTag,
                    rating = p.averageRating.toString(),
                    reviews = p.reviewsCountText.filter { it.isDigit() },
                    imageModel = p.imageUrl ?: p.imageRes,
                    onClick = { }
                )
            }
            item {
                val p = FakeArticle.detailStreetwear
                ProductCatalogCard(
                    title = p.title,
                    category = p.categoryTag,
                    rating = p.averageRating.toString(),
                    reviews = p.reviewsCountText.filter { it.isDigit() },
                    imageModel = p.imageUrl ?: p.imageRes,
                    onClick = { }
                )
            }
        } else {
            // Artículos calificados
            item {
                val p = FakeArticle.abrigoNegro
                ProductCatalogCard(
                    title = p.title,
                    category = p.categoryTag,
                    rating = p.averageRating.toString(),
                    reviews = p.reviewsCountText.filter { it.isDigit() },
                    imageModel = p.imageUrl ?: p.imageRes,
                    onClick = { }
                )
            }
            item {
                val p = FakeArticle.camisetaGymshark
                ProductCatalogCard(
                    title = p.title,
                    category = p.categoryTag,
                    rating = p.averageRating.toString(),
                    reviews = p.reviewsCountText.filter { it.isDigit() },
                    imageModel = p.imageUrl ?: p.imageRes,
                    onClick = { }
                )
            }
        }
    }
}
