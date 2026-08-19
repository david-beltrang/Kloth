package com.example.kloth.ui.screens.notification

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

// Ajusta este import al Composable de tema que ya tienen en el proyecto
// (el que envuelve MaterialTheme con su ColorScheme claro/oscuro).
import com.example.kloth.ui.theme.KlothTheme

/**
 * Previews de NotificacionesScreen. Separados del archivo de la pantalla
 * para no ensuciarlo con código que solo se usa en el editor de Android
 * Studio (nunca se compila dentro del APK final en release).
 */
@Preview(name = "Notificaciones - Claro", showBackground = true)
@Composable
private fun NotificacionesScreenPreviewClaro() {
    KlothTheme(darkTheme = false) {
        NotificacionesScreen()
    }
}

@Preview(name = "Notificaciones - Oscuro", showBackground = true)
@Composable
private fun NotificacionesScreenPreviewOscuro() {
    KlothTheme(darkTheme = true) {
        NotificacionesScreen()
    }
}
