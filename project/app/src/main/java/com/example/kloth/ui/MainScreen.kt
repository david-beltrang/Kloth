package com.example.kloth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kloth.R
import com.example.kloth.navigation.KlothNavGraph
import com.example.kloth.navigation.Screen
import com.example.kloth.ui.screens.editProfile.components.EditProfileTopBar
import com.example.kloth.ui.screens.explore.components.TopAppBar
import com.example.kloth.ui.screens.feed.components.FeedTabRow
import com.example.kloth.ui.utils.KlothBottomNavigation
import com.example.kloth.ui.utils.KlothTopAppBar

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            when (currentRoute) {
                Screen.Feed.route -> {
                    Column {
                        KlothTopAppBar()
                        FeedTabRow()
                    }
                }
                Screen.Explore.route -> {
                    TopAppBar()
                }
                Screen.Profile.route -> {
                    TopAppBar(title = stringResource(R.string.title_profile))
                }
                Screen.EditProfile.route -> {
                    EditProfileTopBar(
                        onCancel = { navController.popBackStack() },
                        onSave = { /* TODO */ }
                    )
                }
            }
        },
        bottomBar = {
            val selectedItem = when (currentRoute) {
                Screen.Feed.route -> 0
                Screen.Explore.route -> 1
                Screen.Add.route -> 2
                Screen.Notifications.route -> 3
                Screen.Profile.route -> 4
                Screen.EditProfile.route -> 4
                else -> -1
            }

            if (currentRoute?.startsWith("detail") == true) {
                Column {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )
                    KlothBottomNavigation(navController = navController)
                }
            } else if (selectedItem != -1) {
                KlothBottomNavigation(navController = navController, selectedItem = selectedItem)
            }
        }
    ) { innerPadding ->
        KlothNavGraph(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}
