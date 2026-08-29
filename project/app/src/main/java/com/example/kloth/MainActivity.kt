package com.example.kloth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kloth.ui.theme.KlothTheme

/**
 * Punto de entrada principal de la aplicación.
 * Mantiene el código mínimo delegando la orquestación a [KlothApp].
 */
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
