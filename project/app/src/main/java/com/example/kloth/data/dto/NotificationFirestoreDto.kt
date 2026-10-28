package com.example.kloth.data.dto

import com.google.firebase.Timestamp

// Esquema escrito por Cloud Functions en users/{userId}/notifications/{eventId}.
data class NotificationFirestoreDto(
    val actorId: String = "",
    val actorName: String = "",
    val actorImageUrl: String? = null,
    val type: String = "",
    val reviewId: String? = null,
    val thumbnailUrl: String? = null,
    val createdAt: Timestamp? = null,
    val isRead: Boolean = false
)
