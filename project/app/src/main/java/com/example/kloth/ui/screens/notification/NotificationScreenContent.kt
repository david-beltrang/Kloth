package com.example.kloth.ui.screens.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.screens.notification.components.NotificacionItem
import com.example.kloth.ui.screens.notification.components.NotificacionesDataLocal

@Composable
fun NotificationScreenContent(
    modifier: Modifier = Modifier
) {
    var notificaciones by remember { mutableStateOf(NotificacionesDataLocal.notificacionesEjemplo) }

    LazyColumn(
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(notificaciones, key = { it.id }) { notificacion ->
            NotificacionItem(
                notificacion = notificacion,
                onToggleSeguir = { id ->
                    notificaciones = notificaciones.map {
                        if (it.id == id) it.copy(estaSiguiendo = !it.estaSiguiendo) else it
                    }
                }
            )
        }
    }
}
