package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.dto.NotificationFirestoreDto
import com.example.kloth.data.datasource.services.NotificationRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NotificationFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : NotificationRemoteDataSource {
    // El listener de Firestore mantiene la lista y el contador sincronizados en tiempo real.
    override fun observeNotifications(userId: String): Flow<List<Pair<String, NotificationFirestoreDto>>> = callbackFlow {
        val listener = db.collection("users").document(userId)
            .collection(NOTIFICATIONS_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(30)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val notifications = snapshot.documents.mapNotNull { document ->
                        document.toObject(NotificationFirestoreDto::class.java)?.let {
                            document.id to it
                        }
                    }
                    trySend(notifications)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun markNotificationsRead(userId: String, notificationIds: List<String>) {
        if (notificationIds.isEmpty()) return
        // Un batch agrupa las actualizaciones y evita una solicitud por notificacion.
        val userNotifications = db.collection("users").document(userId)
            .collection(NOTIFICATIONS_COLLECTION)
        val batch = db.batch()
        notificationIds.forEach { id ->
            batch.update(userNotifications.document(id), "isRead", true)
        }
        batch.commit().await()
    }

    private companion object {
        const val NOTIFICATIONS_COLLECTION = "notifications"
    }
}
