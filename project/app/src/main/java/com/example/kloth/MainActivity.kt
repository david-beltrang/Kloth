package com.example.kloth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.kloth.navigation.AppNavGraph
import com.example.kloth.ui.theme.KlothTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KlothTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}