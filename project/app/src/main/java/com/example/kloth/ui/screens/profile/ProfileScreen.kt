package com.example.kloth.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.kloth.navigation.Screen

@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ProfileScreenContent(
        navController = navController,
        onEditProfileClick = {
            navController.navigate(Screen.EditProfile.route)
        },
        modifier = modifier
    )
}
