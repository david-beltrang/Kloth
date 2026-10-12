package com.example.kloth.ui.screens.notification

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kloth.ui.theme.KlothTheme

@Preview(name = "Notificaciones - Claro", showBackground = true, showSystemUi = true)
@Composable
private fun NotificacionesScreenPreviewClaro() {
    KlothTheme(darkTheme = false) {
        NotificationScreen(notificationViewModel = viewModel())
    }
}

@Preview(
    name = "Notificaciones - Oscuro",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NotificacionesScreenPreviewOscuro() {
    KlothTheme(darkTheme = true) {
        NotificationScreen(notificationViewModel = viewModel())
    }
}