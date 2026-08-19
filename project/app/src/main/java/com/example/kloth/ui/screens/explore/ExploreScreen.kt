package com.example.kloth.ui.screens.explore

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ExploreScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ExploreScreenContent(navController = navController, modifier = modifier)
}
