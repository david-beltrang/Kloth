package com.example.kloth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.kloth.ui.screens.createArticle.CreateArticleScreen
import com.example.kloth.ui.screens.detail.ItemDetailScreen
import com.example.kloth.ui.screens.explore.ExploreScreen
import com.example.kloth.ui.screens.feed.FeedScreen
import com.example.kloth.ui.screens.notification.NotificationScreen
import com.example.kloth.ui.screens.profile.ProfileScreen
import com.example.kloth.ui.screens.review.ReviewScreen
import com.example.kloth.ui.utils.KlothBottomNavigation
import com.example.kloth.ui.utils.KlothTopAppBar
import com.example.kloth.ui.theme.KlothTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KlothTheme {
                KlothApp()
            }
        }
    }
}

@Composable
fun KlothApp() {
    var currentScreen by remember { mutableStateOf("Feed") }


    Scaffold(
        topBar = {
            if (currentScreen != "EditProfile") {
                KlothTopAppBar()
            }
        },
        bottomBar = {
            KlothBottomNavigation(
                selectedItem = when (currentScreen) {
                    "Feed" -> 0
                    "Explore" -> 1
                    "Create" -> 2
                    "Notifications" -> 3
                    "Profile", "EditProfile" -> 4
                    else -> 0
                },
                onItemSelected = { index ->
                    currentScreen = when (index) {
                        0 -> "Feed"
                        1 -> "Explore"
                        2 -> "Create"
                        3 -> "Notifications"
                        4 -> "Profile"
                        else -> "Feed"
                    }
                }
            )
        }
    ) { innerPadding ->
        ReviewScreen(
            modifier = Modifier.padding(innerPadding)
        )
        /*val modifier = Modifier.padding(innerPadding)
        when (currentScreen) {
            "Feed" -> FeedScreen(modifier = modifier)
            "Explore" -> ExploreScreen(modifier = modifier)
            "Create" -> CreateArticleScreen(modifier = modifier)
            "Notifications" -> FeedScreen(modifier = modifier) // Placeholder
            "Profile" -> ProfileScreen(modifier = modifier)
        }*/
    }
}
