package com.example.kloth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.screens.ExploreScreen

// Actividad principal: punto de entrada de la app. Infla la pantalla de Explorar.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExploreScreen()
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun MainPreview() {
    ExploreScreen()
}