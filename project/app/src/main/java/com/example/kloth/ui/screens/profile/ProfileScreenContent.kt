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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.detail.components.CatalogCard
import com.example.kloth.ui.screens.feed.components.ProfileTabRow
import com.example.kloth.ui.screens.profile.components.ProfileHeaderSection

@Composable
fun ProfileScreenContent(
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

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
                    onTabSelected = { selectedTabIndex = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (selectedTabIndex == 0) {
            item {
                CatalogCard(
                    title = stringResource(R.string.product_abrigo),
                    category = stringResource(R.string.category_prendas),
                    rating = "4.9",
                    reviews = "128",
                    imageRes = R.drawable.abrigo_negro,
                    onClick = { }
                )
            }
            item {
                CatalogCard(
                    title = stringResource(R.string.product_gym),
                    category = stringResource(R.string.category_outfits),
                    rating = "4.8",
                    reviews = "84",
                    imageRes = R.drawable.camiseta_gymshark,
                    onClick = { }
                )
            }
            item {
                CatalogCard(
                    title = stringResource(R.string.product_barcelona),
                    category = stringResource(R.string.category_prendas),
                    rating = "5.0",
                    reviews = "42",
                    imageRes = R.drawable.camiseta_barcelona,
                    onClick = { }
                )
            }
        } else {
            item {
                CatalogCard(
                    title = stringResource(R.string.product_bolso),
                    category = stringResource(R.string.category_prendas),
                    rating = "4.7",
                    reviews = "215",
                    imageRes = R.drawable.bolso_cuero,
                    onClick = { }
                )
            }
            item {
                CatalogCard(
                    title = stringResource(R.string.product_abrigo),
                    category = stringResource(R.string.category_prendas),
                    rating = "4.9",
                    reviews = "128",
                    imageRes = R.drawable.abrigo_negro,
                    onClick = { }
                )
            }
            item {
                CatalogCard(
                    title = stringResource(R.string.product_barcelona),
                    category = stringResource(R.string.category_marcas),
                    rating = "5.0",
                    reviews = "42",
                    imageRes = R.drawable.camiseta_barcelona,
                    onClick = { }
                )
            }
            item {
                CatalogCard(
                    title = stringResource(R.string.product_gym),
                    category = stringResource(R.string.category_eventos),
                    rating = "4.8",
                    reviews = "84",
                    imageRes = R.drawable.camiseta_gymshark,
                    onClick = { }
                )
            }
        }
    }
}
