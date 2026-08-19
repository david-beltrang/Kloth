package com.example.kloth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kloth.ui.screens.detail.ItemDetailScreen
import com.example.kloth.ui.screens.editProfile.EditProfileScreen
import com.example.kloth.ui.screens.explore.ExploreScreen
import com.example.kloth.ui.screens.feed.FeedScreen
import com.example.kloth.ui.screens.profile.ProfileScreen

sealed class Screen(val route: String) {
    object Feed : Screen("feed")
    object Explore : Screen("explore")
    object Add : Screen("add")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
    object EditProfile : Screen("editProfile")
    object Detail : Screen("detail/{productId}") {
        fun createRoute(productId: String) = "detail/$productId"
    }
}

@Composable
fun KlothNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Feed.route,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(Screen.Feed.route) {
            FeedScreen(navController = navController)
        }
        composable(Screen.Explore.route) {
            ExploreScreen(navController = navController)
        }
        composable(Screen.Add.route) {
            // Placeholder or AddScreen if exists
        }
        composable(Screen.Notifications.route) {
            // Placeholder or NotificationsScreen if exists
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen()
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ItemDetailScreen(productId = productId, navController = navController)
        }
    }
}
