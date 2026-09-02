package com.example.kloth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kloth.ui.navigation.AppNavigation
import com.example.kloth.ui.navigation.AppRoutes
import com.example.kloth.ui.utils.KlothBottomNavigation
import com.example.kloth.ui.utils.KlothTopAppBar

// orquestador principal, define el scaffold con topbar y bottombar
@Composable
fun KlothApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            // Se oculta la barra superior en pantallas que no la requieren (Login, Edición)
            if (currentRoute != AppRoutes.Login.route &&
                currentRoute != AppRoutes.EditProfile.route
            ) {
                KlothTopAppBar()
            }
        },
        bottomBar = {
            // Se oculta la navegación inferior en la pantalla de Login
            if (currentRoute != AppRoutes.Login.route) {
                KlothBottomNavigation(
                    selectedItem = when (currentRoute) {
                        AppRoutes.Feed.route -> 0
                        AppRoutes.Explore.route -> 1
                        AppRoutes.CreateArticle.route -> 2
                        AppRoutes.Notifications.route -> 3
                        AppRoutes.Profile.route, AppRoutes.EditProfile.route -> 4
                        else -> -1
                    },
                    onItemSelected = { index ->
                        val target = when (index) {
                            0 -> AppRoutes.Feed
                            1 -> AppRoutes.Explore
                            2 -> AppRoutes.CreateArticle
                            3 -> AppRoutes.Notifications
                            else -> AppRoutes.Profile
                        }
                        navController.navigate(target.route) {
                            // Vuelve a la raiz del area principal preservando el estado de cada tab
                            popUpTo(AppRoutes.Feed.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
