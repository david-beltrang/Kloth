package com.example.kloth.ui.utils

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.navigation.AppRoutes
import com.example.kloth.ui.theme.KlothTheme


@Composable
fun KlothBottomNavigation(
    currentRoute: String?,
    onTabClick: (AppRoutes) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(AppRoutes.Feed, Icons.Filled.Home, Icons.Outlined.Home, R.string.nav_home),
        BottomNavItem(AppRoutes.Explore, Icons.Filled.Search, Icons.Outlined.Search, R.string.nav_explore),
        BottomNavItem(AppRoutes.CreateArticle, Icons.Filled.AddBox, Icons.Outlined.AddBox, R.string.nav_create),
        BottomNavItem(AppRoutes.Notifications, Icons.Filled.Notifications, Icons.Outlined.Notifications, R.string.nav_alerts),
        BottomNavItem(AppRoutes.Profile, Icons.Filled.Person, Icons.Outlined.Person, R.string.nav_profile)
    )

 NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val isSelected = when (item.route) {
                AppRoutes.Profile ->
                    currentRoute == AppRoutes.Profile.route ||
                        currentRoute == AppRoutes.EditProfile.route ||
                        currentRoute?.startsWith(AppRoutes.UserProfile.route.substringBefore("/")) == true
                else -> currentRoute == item.route.route
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabClick(item.route) },
                icon = {
                    if (item.route == AppRoutes.Notifications) {
                        NotificationCount(
                            count = 5,
                            icon = if (isSelected) item.selectedIcon else item.unselectedIcon
                        )
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = stringResource(item.labelRes)
                        )
                    }
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                )
            )
        }
    }
}

private data class BottomNavItem(
    val route: AppRoutes,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val labelRes: Int
)

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun KlothBottomNavigationPreview() {
    KlothTheme(darkTheme = false) {
        KlothBottomNavigation(
            currentRoute = AppRoutes.Feed.route,
            onTabClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KlothBottomNavigationDarkPreview() {
    KlothTheme(darkTheme = true) {
        KlothBottomNavigation(
            currentRoute = AppRoutes.Profile.route,
            onTabClick = {}
        )
    }
}
