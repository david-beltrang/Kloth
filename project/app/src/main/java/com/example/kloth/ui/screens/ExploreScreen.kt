package com.example.kloth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.components.*

@Composable
fun ExploreScreen() {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val categories = listOf(
        stringResource(R.string.category_prendas),
        stringResource(R.string.category_outfits),
        stringResource(R.string.category_marcas),
        stringResource(R.string.category_eventos)
    )

    Scaffold(
        topBar = { TopAppBar() },
        bottomBar = {
            Column {
                Divider(color = colorResource(id = R.color.outline_variant), thickness = 1.dp)
                BottomBar()
            }
        },
        containerColor = colorResource(id = R.color.surface)
    ) { paddingValues ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 24.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {

            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.surface).copy(alpha = 0.9f))
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    SearchBar()
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(end = 16.dp)
                    ) {
                        items(categories.size) { index ->
                            CategoryChip(
                                text = categories[index],
                                isSelected = selectedCategoryIndex == index,
                                onClick = { selectedCategoryIndex = index }
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
                                        tint = colorResource(id = R.color.on_surface_variant)
                                    )
                                }
                            )
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(colorResource(id = R.color.outline_variant))
                            )
                        }
                        item {
                            FilterChip(text = stringResource(R.string.filter_price), trailingIcon = { ExpandIcon() })
                        }
                        item {
                            FilterChip(text = stringResource(R.string.filter_color), trailingIcon = { ExpandIcon() })
                        }
                        item {
                            FilterChip(text = stringResource(R.string.filter_size), trailingIcon = { ExpandIcon() })
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item { CatalogCard(stringResource(R.string.product_abrigo), stringResource(R.string.category_prendas), "4.9", "128", imageRes = R.drawable.abrigo_negro) }
            item { CatalogCard(stringResource(R.string.product_gym), stringResource(R.string.category_prendas), "4.8", "84", imageRes = R.drawable.camiseta_gymshark) }
            item { CatalogCard(stringResource(R.string.product_barcelona), stringResource(R.string.category_prendas), "5.0", "42", imageRes = R.drawable.camiseta_barcelona) }
            item { CatalogCard(stringResource(R.string.product_bolso), stringResource(R.string.category_prendas), "4.7", "215", imageRes = R.drawable.bolso_cuero) }
        }
    }
}

@Composable
private fun ExpandIcon() {
    Icon(
        imageVector = Icons.Outlined.KeyboardArrowDown,
        contentDescription = null,
        modifier = Modifier.size(16.dp),
        tint = colorResource(id = R.color.on_surface_variant)
    )
}