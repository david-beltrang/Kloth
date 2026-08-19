package com.example.kloth.ui.screens.notification

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.ui.theme.KlothTheme

@Preview(name = "Notificaciones - Claro", showBackground = true)
@Composable
private fun NotificacionesScreenPreviewClaro() {
    KlothTheme(darkTheme = false) {
        NotificationScreen()
    }
}

@Preview(name = "Notificaciones - Oscuro", showBackground = true)
@Composable
private fun NotificacionesScreenPreviewOscuro() {
    KlothTheme(darkTheme = true) {
        NotificationScreen()
    }
}
