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

/**
 * Orquestador principal de la aplicación.
 * Define la estructura base con TopBar y BottomBar sincronizadas con la navegación.
 */
@Composable
fun KlothApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            // La barra superior se oculta en pantallas de autenticación o edición
            if (currentRoute != AppRoutes.Login.route &&
                currentRoute != AppRoutes.EditProfile.route
            ) {
                KlothTopAppBar()
            }
        },
        bottomBar = {
            // La navegación inferior solo se muestra en el flujo principal de la app
            if (currentRoute != AppRoutes.Login.route && 
                currentRoute != AppRoutes.Register.route &&
                currentRoute != AppRoutes.ForgotPassword.route
            ) {
                KlothBottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
