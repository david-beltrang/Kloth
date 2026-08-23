package com.example.kloth.ui.screens.notification

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.data.NotificacionesDataLocal

@Composable
fun NotificationScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    var notificaciones by remember { mutableStateOf(NotificacionesDataLocal.notificacionesEjemplo) }

    NotificationScreenContent(
        notificaciones = notificaciones,
        onToggleSeguir = { id ->
            notificaciones = notificaciones.map {
                if (it.id == id) it.copy(estaSiguiendo = !it.estaSiguiendo) else it
            }
        },
        contentPadding = contentPadding,
        modifier = modifier
    )
}