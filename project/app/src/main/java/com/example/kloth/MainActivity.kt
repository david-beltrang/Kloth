package com.example.kloth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kloth.ui.screens.detail.ItemDetailScreen
import com.example.kloth.ui.screens.explore.ExploreScreen
import com.example.kloth.ui.screens.feed.FeedScreen
import com.example.kloth.ui.screens.profile.ProfileScreen
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.screens.createArticlle.CreateArticleScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KlothTheme {
                //CreateArticleScreen()
                FeedScreen()
                //ExploreScreen()
                //ItemDetailScreen(productId = "abrigo_negro")
                //ProfileScreen()
            }
        }
    }
}
