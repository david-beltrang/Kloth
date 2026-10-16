package com.example.kloth.data.dto

// DTO de entrada: copia exacta del JSON que devuelve GET /reviews y GET /articles/{id}/reviews
data class ReviewDto(
    val reviewId: Int,
    val userId: Int,
    val articleId: Int,
    val rating: Int,
    val text: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val eliminated: Boolean?
)
