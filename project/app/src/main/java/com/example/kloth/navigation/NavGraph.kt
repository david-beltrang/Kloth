package com.example.kloth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kloth.ui.screens.explore.ExploreScreen
import com.example.kloth.ui.screens.detail.ItemDetailScreen

sealed class Screen(val route: String) {
    object Explore : Screen("explore_screen")
    object Detail : Screen("detail_screen/{productId}") {
        fun createRoute(productId: String) = "detail_screen/$productId"
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Explore.route
    ) {
        composable(Screen.Explore.route) {
            ExploreScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.Detail.createRoute(productId))
                }
            )
        }

        composable(Screen.Detail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ItemDetailScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}