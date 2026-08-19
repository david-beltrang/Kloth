package com.example.kloth.ui.screens.notification.components

data class NotificacionUI(
    val id: String,
    val nombreUsuario: String,
    val descripcion: String, // ej: "comentó tu reseña"
    val tiempo: String, // ej: "Hace 18 min"
    val tipo: TipoNotificacion,
    val tieneMiniatura: Boolean = false,
    val noLeida: Boolean = false,
    val estaSiguiendo: Boolean = false
)

enum class TipoNotificacion {
    NUEVO_SEGUIDOR,
    COMENTARIO,
    LIKE,
    MENCION,
    GUARDADO
}
