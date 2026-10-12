package com.example.kloth.data.dto

/**
 * Estructura exacta de una reseña (`GET /reviews`, `GET /articles/:id/reviews`,
 * `GET /reviews/:id`, `GET /reviews?userId=`).
 */
data class ReviewDto(
    val reviewId: Int,
    val userId: Int,
    val articleId: Int,
    val rating: Int,
    val text: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val likesCount: Int = 0,
    val eliminated: Boolean = false
)

/**
 * Cuerpo de `POST /reviews`.
 */
data class CreateReviewDto(
    val userId: Int,
    val articleId: Int,
    val rating: Int,
    val text: String? = null,
    val createdAt: String? = null,
    val eliminated: Boolean = false
)

/**
 * Cuerpo de `PUT /reviews/:id`. Los campos en null no se serializan
 * (Gson omite los null) y por lo tanto no se modifican en el backend.
 */
data class UpdateReviewDto(
    val rating: Int? = null,
    val text: String? = null,
    val likesCount: Int? = null,
    val eliminated: Boolean? = null
)
