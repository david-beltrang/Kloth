package com.example.kloth.ui.screens.explore

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.kloth.R
import com.example.kloth.ui.screens.detail.components.CatalogCard
import com.example.kloth.ui.screens.explore.components.CategoryChip
import com.example.kloth.ui.screens.explore.components.FilterChip
import com.example.kloth.ui.screens.explore.components.SearchBar
import com.example.kloth.ui.screens.explore.components.TopAppBar
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.utils.KlothBottomNavigation

@Preview(
    name = "Explore Screen - Light",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ExploreScreenPreview() {
    KlothTheme {
        ExploreScreen(navController = rememberNavController())
    }
}

@Preview(name = "Explore Components - Top Bar", showBackground = true)
@Composable
fun ExploreTopBarPreview() {
    KlothTheme {
        TopAppBar(title = stringResource(R.string.title_explore))
    }
}

@Preview(name = "Explore Components - Bottom Nav", showBackground = true)
@Composable
fun ExploreBottomNavPreview() {
    KlothTheme {
        KlothBottomNavigation(navController = rememberNavController(), selectedItem = 1)
    }
}

@Preview(name = "Explore Components - Search Bar", showBackground = true)
@Composable
fun ExploreSearchBarPreview() {
    KlothTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SearchBar()
        }
    }
}

@Preview(name = "Explore Components - Category Chips", showBackground = true)
@Composable
fun ExploreCategoryChipsPreview() {
    KlothTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryChip(
                text = stringResource(R.string.category_prendas),
                isSelected = true,
                onClick = {}
            )
            CategoryChip(
                text = stringResource(R.string.category_outfits),
                isSelected = false,
                onClick = {}
            )
        }
    }
}

@Preview(name = "Explore Components - Filter Chips", showBackground = true)
@Composable
fun ExploreFilterChipsPreview() {
    KlothTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                text = stringResource(R.string.filter_tune),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
            FilterChip(
                text = stringResource(R.string.filter_price),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
    }
}

@Preview(name = "Explore Components - Catalog Card", showBackground = true)
@Composable
fun ExploreCatalogCardPreview() {
    KlothTheme {
        Box(modifier = Modifier.padding(16.dp).width(180.dp)) {
            CatalogCard(
                title = stringResource(R.string.product_abrigo),
                category = stringResource(R.string.category_prendas),
                rating = "4.9",
                reviews = "128",
                imageRes = R.drawable.abrigo_negro,
                onClick = {}
            )
        }
    }
}
