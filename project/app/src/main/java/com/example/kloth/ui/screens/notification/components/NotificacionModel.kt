package com.example.kloth.ui.screens.notification.componentes

/**
 * Entidad de UI que representa una notificación tal como se muestra en pantalla.
 * OJO: esto NO es la entidad de dominio/BD (esa vive en el modelo de datos del
 * proyecto, ver enum TipoNotificacion del diagrama de clases). Esta es una
 * representación simplificada pensada solo para renderizar la fila en Compose.
 *
 * Nota: el diagrama de clases del proyecto define TipoNotificacion con los
 * valores NUEVO_SEGUIDOR, COMENTARIO, LIKE, RESPUESTA. Este mock de UI agrega
 * MENCION y GUARDADO porque aparecen en el diseño de Figma; si el equipo de
 * backend no los va a soportar, hay que reconciliar esto con el dominio real.
 */
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
