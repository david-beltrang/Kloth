package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.components.CatalogCard
import com.example.kloth.ui.components.TopAppBar
import com.example.kloth.ui.components.utils.KlothBottomNavigation
import com.example.kloth.ui.components.feedComponents.ProfileTabRow

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = stringResource(R.string.title_profile))
        },
        bottomBar = {
            KlothBottomNavigation(selectedItem = 4)
        },
        containerColor = MaterialTheme.colorScheme.surface
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
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    ProfileHeaderSection()

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileTabRow(
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (selectedTabIndex == 0) {
                // RF-06: publicaciones propias (solo Prendas / Outfits; no catálogo)
                item {
                    CatalogCard(
                        title = stringResource(R.string.product_abrigo),
                        category = stringResource(R.string.category_prendas),
                        rating = "4.9",
                        reviews = "128",
                        imageRes = R.drawable.abrigo_negro,
                        onClick = { /* Navegación desactivada */ }
                    )
                }
                item {
                    CatalogCard(
                        title = stringResource(R.string.product_gym),
                        category = stringResource(R.string.category_outfits),
                        rating = "4.8",
                        reviews = "84",
                        imageRes = R.drawable.camiseta_gymshark,
                        onClick = { /* Navegación desactivada */ }
                    )
                }
                item {
                    CatalogCard(
                        title = stringResource(R.string.product_barcelona),
                        category = stringResource(R.string.category_prendas),
                        rating = "5.0",
                        reviews = "42",
                        imageRes = R.drawable.camiseta_barcelona,
                        onClick = { /* Navegación desactivada */ }
                    )
                }
            } else {
                // RF-07: listado de artículos que el usuario ha calificado
                item {
                    CatalogCard(
                        title = stringResource(R.string.product_bolso),
                        category = stringResource(R.string.category_prendas),
                        rating = "4.7",
                        reviews = "215",
                        imageRes = R.drawable.bolso_cuero,
                        onClick = { /* Navegación desactivada */ }
                    )
                }
                item {
                    CatalogCard(
                        title = stringResource(R.string.product_abrigo),
                        category = stringResource(R.string.category_prendas),
                        rating = "4.9",
                        reviews = "128",
                        imageRes = R.drawable.abrigo_negro,
                        onClick = { /* Navegación desactivada */ }
                    )
                }
                item {
                    CatalogCard(
                        title = stringResource(R.string.product_barcelona),
                        category = stringResource(R.string.category_marcas),
                        rating = "5.0",
                        reviews = "42",
                        imageRes = R.drawable.camiseta_barcelona,
                        onClick = { /* Navegación desactivada */ }
                    )
                }
                item {
                    CatalogCard(
                        title = stringResource(R.string.product_gym),
                        category = stringResource(R.string.category_eventos),
                        rating = "4.8",
                        reviews = "84",
                        imageRes = R.drawable.camiseta_gymshark,
                        onClick = { /* Navegación desactivada */ }
                    )
                }
            }
        }
    }
}
