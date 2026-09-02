package com.example.kloth.ui.screens.notification

import com.example.kloth.data.NotificacionUI

data class NotificationState(
    val notificaciones: List<NotificacionUI> = emptyList(),
    val isLoading: Boolean = false
)
