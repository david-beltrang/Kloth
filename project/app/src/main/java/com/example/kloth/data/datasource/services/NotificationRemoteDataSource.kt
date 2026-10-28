package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.NotificationFirestoreDto
import kotlinx.coroutines.flow.Flow

// Contrato para observar las notificaciones del usuario y marcarlas como leidas.
interface NotificationRemoteDataSource {
    fun observeNotifications(userId: String): Flow<List<Pair<String, NotificationFirestoreDto>>>
    suspend fun markNotificationsRead(userId: String, notificationIds: List<String>)
}
