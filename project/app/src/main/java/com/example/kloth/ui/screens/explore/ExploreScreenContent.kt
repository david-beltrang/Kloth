package com.example.kloth.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.data.FakeArticle
import com.example.kloth.ui.screens.detail.components.ProductCatalogCard
import com.example.kloth.ui.screens.explore.components.CategoryChip
import com.example.kloth.ui.screens.explore.components.FilterChip
import com.example.kloth.ui.screens.explore.components.SearchBar

@Composable
fun ExploreScreenContent(
    modifier: Modifier = Modifier,
    selectedCategoryIndex: Int,
    onCategorySelected: (Int) -> Unit,
    onProductClick: (String) -> Unit = {},
) {
    val categoryResIds = listOf(
        R.string.category_prendas,
        R.string.category_outfits,
        R.string.category_marcas,
        R.string.category_eventos,
    )

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
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                SearchBar()
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(categoryResIds.size) { index ->
                        CategoryChip(
                            text = stringResource(categoryResIds[index]),
                            isSelected = selectedCategoryIndex == index,
                            onClick = { onCategorySelected(index) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    item {
                        FilterChip(
                            text = stringResource(R.string.filter_tune),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Tune,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(24.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                    item {
                        FilterChip(
                            text = stringResource(R.string.filter_price),
                            trailingIcon = {
                                ExpandIcon()
                            }
                        )
                    }
                    item {
                        FilterChip(
                            text = stringResource(R.string.filter_color),
                            trailingIcon = {
                                ExpandIcon()
                            }
                        )
                    }
                    item {
                        FilterChip(
                            text = stringResource(R.string.filter_size),
                            trailingIcon = {
                                ExpandIcon()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Ahora mostramos dinámicamente todos los productos del catálogo
        items(FakeArticle.allProducts) { product ->
            ProductCatalogCard(
                title = product.title,
                category = product.categoryTag,
                rating = product.averageRating.toString(),
                reviews = product.reviewsCountText.filter { it.isDigit() }, // Limpiamos "(128 reseñas)" a "128"
                imageModel = product.imageUrl ?: product.imageRes,
                onClick = { onProductClick(product.id) }
            )
        }
    }
}

@Composable
private fun ExpandIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Outlined.KeyboardArrowDown,
        contentDescription = null,
        modifier = modifier.size(16.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
