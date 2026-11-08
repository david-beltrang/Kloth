package com.example.kloth.data.repository

import android.text.format.DateUtils
import com.example.kloth.data.datasource.services.NotificationRemoteDataSource
import com.example.kloth.data.dto.NotificationFirestoreDto
import com.example.kloth.data.local.NotificacionUI
import com.example.kloth.data.local.TipoNotificacion
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Convierte documentos de Firestore al modelo que consume la interfaz.
@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDataSource: NotificationRemoteDataSource
) {
    // Transforma los documentos remotos al modelo que presenta Compose.
    fun observeNotifications(userId: String): Flow<List<NotificacionUI>> =
        notificationDataSource.observeNotifications(userId).map { documents ->
            documents.map { (id, notification) -> notification.toUiModel(id) }
        }

    // Actualiza en lote los IDs que la pantalla mostro como no leidos.
    suspend fun markNotificationsRead(userId: String, notificationIds: List<String>) =
        notificationDataSource.markNotificationsRead(userId, notificationIds)

    private fun NotificationFirestoreDto.toUiModel(id: String): NotificacionUI {
        // Convierte la marca de tiempo del servidor en una etiqueta relativa a la hora actual.
        val createdAtMillis = createdAt?.toDate()?.time
        val timeText = createdAtMillis?.let {
            DateUtils.getRelativeTimeSpanString(
                it,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ).toString()
        }.orEmpty()

        // Conserva los datos del actor y la publicacion para el avatar y la miniatura.
        return NotificacionUI(
            id = id,
            actorId = actorId,
            nombreUsuario = actorName,
            descripcion = "",
            tiempo = timeText,
            tipo = runCatching { TipoNotificacion.valueOf(type) }
                .getOrDefault(TipoNotificacion.COMENTARIO),
            avatarUrl = actorImageUrl,
            thumbnailUrl = thumbnailUrl,
            tieneMiniatura = !thumbnailUrl.isNullOrBlank(),
            noLeida = !isRead
        )
    }
}
