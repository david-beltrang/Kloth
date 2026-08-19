package com.example.kloth.ui.screens.notification.componentes

/**
 * Datos locales quemados de la entidad de UI NotificacionUI, usados mientras
 * no hay conexión real a Firestore/API REST. Reemplazar por el repositorio
 * real en la siguiente entrega.
 */
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
        ),
        NotificacionUI(
            id = "5",
            nombreUsuario = "Marcus Lew",
            descripcion = "mencionó tu perfil en un comentario",
            tiempo = "Hace 5h",
            tipo = TipoNotificacion.MENCION,
            tieneMiniatura = true
        ),
        NotificacionUI(
            id = "6",
            nombreUsuario = "Elias Thorne",
            descripcion = "guardó tu publicación",
            tiempo = "Ayer",
            tipo = TipoNotificacion.GUARDADO,
            tieneMiniatura = true
        )
    )
}
