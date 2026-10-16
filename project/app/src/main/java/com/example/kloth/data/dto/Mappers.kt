package com.example.kloth.data.dto

import com.example.kloth.R
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ProductDetailData
import com.example.kloth.data.local.ReviewData
import java.time.Duration
import java.time.Instant
import java.util.Locale

// Funciones de mapeo: traducen lo que llega del backend (DTO) a los modelos que ya usa la UI.
// Si el backend cambia un nombre de columna, solo se toca el DTO y este archivo.
// Nota: el backend guarda los textos como CHAR(n) y los devuelve rellenos de espacios, por eso el trim().

fun ArticleDto.toPostItem(creator: UserDto?, reviews: List<ReviewDto>, usersById: Map<Int, UserDto>): PostItem =
    PostItem(
        id = articleId.toString(),
        creatorName = creator?.username?.trim() ?: DELETED_USER,
        timeAgo = timeAgo(createdAt),
        avatarUrl = creator?.imageURL ?: "",
        commentsCount = reviews.size.toString(),
        tags = emptyList(),
        product = toProductDetailData(reviews, usersById)
    )

fun ArticleDto.toProductDetailData(reviews: List<ReviewDto>, usersById: Map<Int, UserDto>): ProductDetailData =
    ProductDetailData(
        id = articleId.toString(),
        title = title.trim(),
        // El backend todavia no devuelve marca, precio, color ni categoria (tablas de subtipos vacias)
        brand = "",
        price = "",
        categoryTag = typeLabel(typeArticle),
        colorName = "",
        categoryName = "",
        description = description.trim(),
        imageUrl = imageArticle,
        averageRating = averageRating(reviews),
        reviewsCountText = reviewsCountText(reviews.size),
        reviewsList = reviews.map { it.toReviewData(usersById[it.userId]) }
    )

fun ReviewDto.toReviewData(author: UserDto?): ReviewData =
    ReviewData(
        id = reviewId.toString(),
        authorName = author?.username?.trim() ?: DELETED_USER,
        timeAgo = timeAgo(createdAt),
        avatarRes = R.drawable.profile, // se muestra si el autor no tiene foto
        avatarUrl = author?.imageURL,
        rating = rating.toFloat(),
        reviewText = text?.trim() ?: ""
    )

private const val DELETED_USER = "Usuario eliminado"

// PRENDA -> "Prenda", igual que la etiqueta que ya mostraba la app
private fun typeLabel(typeArticle: String): String =
    typeArticle.trim().lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }

private fun averageRating(reviews: List<ReviewDto>): Float {
    if (reviews.isEmpty()) return 0f
    val average = reviews.map { it.rating }.average()
    return Math.round(average * 10) / 10f // un decimal: 4.5
}

private fun reviewsCountText(count: Int): String =
    if (count == 1) "(1 reseña)" else "($count reseñas)"

// "2026-09-20T00:00:00.000Z" -> "Hace 17 días"
private fun timeAgo(createdAt: String?): String {
    if (createdAt.isNullOrBlank()) return ""
    return try {
        val minutes = Duration.between(Instant.parse(createdAt), Instant.now()).toMinutes()
        when {
            minutes < 1 -> "Hace un momento"
            minutes < 60 -> "Hace $minutes min"
            minutes < 60 * 24 -> "Hace ${minutes / 60} h"
            minutes < 60 * 24 * 2 -> "Hace 1 día"
            minutes < 60 * 24 * 30 -> "Hace ${minutes / (60 * 24)} días"
            minutes < 60 * 24 * 60 -> "Hace 1 mes"
            else -> "Hace ${minutes / (60 * 24 * 30)} meses"
        }
    } catch (e: Exception) {
        "" // si la fecha no tiene el formato esperado no se muestra
    }
}
