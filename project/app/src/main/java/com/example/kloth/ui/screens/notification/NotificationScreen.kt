package com.example.kloth.ui.screens.notification

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationScreen(
    notificationViewModel: NotificationViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val state by notificationViewModel.uiState.collectAsState()

    NotificationScreenContent(
        // Variables de estado
        notificaciones = state.notificaciones,

        // Métodos del ViewModel para manejar el estado
        onToggleSeguir = { notificationViewModel.onToggleSeguir(it) },

        contentPadding = contentPadding,
        modifier = modifier
    )
}
