package com.example.kloth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kloth.ui.navigation.AppNavigation
import com.example.kloth.ui.navigation.AppRoutes
import com.example.kloth.ui.navigation.NavigationLogic
import com.example.kloth.ui.utils.KlothBottomNavigation
import com.example.kloth.ui.utils.KlothTopAppBar
import com.google.firebase.auth.FirebaseAuth

/**
 * Orquestador principal de la aplicación.
 * Define la estructura base con TopBar y BottomBar sincronizadas con la navegación.
 */
@Composable
fun KlothApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val currentUser = FirebaseAuth.getInstance().currentUser
    val photoUrl: String = currentUser?.photoUrl.toString() ?: ""

    Scaffold(
        topBar = {
            // La barra superior se oculta en pantallas de autenticación o edición
            if (NavigationLogic.shouldShowTopBar(currentRoute)
            ) {
                KlothTopAppBar()
            }
        },
        bottomBar = {
            // La navegación inferior solo se muestra en el flujo principal de la app
            if (NavigationLogic.shouldShowBottomBar(currentRoute)
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
