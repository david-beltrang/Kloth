package com.example.kloth.data.dto

/**
 * Estructura exacta de un comentario de reseña
 * (`GET /reviews/:id/comments`).
 */
data class CommentDto(
    val commentId: Int,
    val reviewId: Int,
    val userId: Int,
    val text: String,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val likesCount: Int = 0,
    val eliminated: Boolean = false
)

/**
 * Cuerpo de `POST /reviews/:id/comments`.
 */
data class CreateCommentDto(
    val userId: Int,
    val text: String,
    val likesCount: Int = 0
)

/**
 * Cuerpo de `PUT /comments/:id`. Los campos en null no se serializan
 * (Gson omite los null) y por lo tanto no se modifican en el backend.
 */
data class UpdateCommentDto(
    val text: String? = null,
    val likesCount: Int? = null,
    val eliminated: Boolean? = null
)
