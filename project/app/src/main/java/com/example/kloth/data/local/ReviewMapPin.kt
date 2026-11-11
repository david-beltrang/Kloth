package com.example.kloth.data.local

// Coordenadas redondeadas a 3 decimales (~110 m) para proteger la privacidad del usuario.
data class Coordinates(val latitude: Double, val longitude: Double)

// Modelo ligero para el pin del mapa: sin tipos de Android ni de MapLibre.
data class ReviewMapPin(
    val reviewId: String,
    val articleId: String,
    val authorId: String,
    val authorName: String,
    val avatarUrl: String?,
    val rating: Float,
    val reviewText: String,
    val timeAgo: String,
    val coordinates: Coordinates
)
