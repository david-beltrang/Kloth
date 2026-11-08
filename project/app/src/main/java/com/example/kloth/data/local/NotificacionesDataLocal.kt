package com.example.kloth.data.local

// Tipos serializados por nombre en Firestore y usados para elegir el texto de la fila.
enum class TipoNotificacion {
    NUEVO_SEGUIDOR,
    NUEVA_RESENA,
    COMENTARIO,
    LIKE
}

// Datos ya preparados por el repositorio para pintar una notificacion en Compose.
data class NotificacionUI(
    val id: String,
    val actorId: String = "",
    val nombreUsuario: String,
    val descripcion: String,
    val tiempo: String,
    val tipo: TipoNotificacion,
    val avatarUrl: String? = null,
    val thumbnailUrl: String? = null,
    val tieneMiniatura: Boolean = false,
    val noLeida: Boolean = false,
    val estaSiguiendo: Boolean = false
)

object NotificacionesDataLocal {
    val notificacionesEjemplo = listOf(
        NotificacionUI(
            id = "1",
            nombreUsuario = "Elias Thorne",
            descripcion = "comenzó a seguirte",
            tiempo = "Hace 2 min",
            tipo = TipoNotificacion.NUEVO_SEGUIDOR,
            noLeida = true,
            estaSiguiendo = false
        ),
        NotificacionUI(
            id = "2",
            nombreUsuario = "Sienna Voss",
            descripcion = "comentó tu reseña",
            tiempo = "Hace 18 min",
            tipo = TipoNotificacion.COMENTARIO,
            tieneMiniatura = true,
            noLeida = true
        ),
        NotificacionUI(
            id = "3",
            nombreUsuario = "Carlos M.",
            descripcion = "dio like a tu reseña",
            tiempo = "Hace 1h",
            tipo = TipoNotificacion.LIKE,
            tieneMiniatura = true
        ),
        NotificacionUI(
            id = "4",
            nombreUsuario = "Nadia Blanc",
            descripcion = "comenzó a seguirte",
            tiempo = "Hace 3h",
            tipo = TipoNotificacion.NUEVO_SEGUIDOR,
            estaSiguiendo = true
        )
    )
}
