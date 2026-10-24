package com.example.kloth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kloth.ui.navigation.AppNavigation
import com.example.kloth.ui.navigation.AppRoutes
import com.example.kloth.ui.navigation.NavigationLogic
import com.example.kloth.ui.utils.KlothBottomNavigation
import com.example.kloth.ui.utils.KlothTopAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import java.util.jar.Manifest.*

/**
 * Orquestador principal de la aplicación.
 * Define la estructura base con TopBar y BottomBar sincronizadas con la navegación.
 */
@Composable
fun KlothApp() {

    FirebaseMessaging.getInstance().token.addOnCompleteListener{ task->
        if(task.isSuccessful){
            Log.d("Token", task.result)
        } else {
            Log.d("Token", "Error getting token")
        }
    }

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val currentUser = FirebaseAuth.getInstance().currentUser
    val photoUrl: String = currentUser?.photoUrl.toString() ?: ""
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Log.d("NotificationPermission", "Permiso de notificación concedido")
            } else {
                Log.d("NotificationPermission", "Permiso de notificación denegado")
            }
        }
    )

    LaunchedEffect(Unit) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

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
