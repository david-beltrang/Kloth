package com.example.kloth.data.dto

import com.example.kloth.R
import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ReviewData
import java.time.Duration
import java.time.Instant
import java.util.Locale

// Funciones de mapeo: traducen lo que llega del backend (DTO) a los modelos que ya usa la UI.
// Si el backend cambia un nombre de columna, solo se toca el DTO y este archivo.
// Nota: el backend guarda los textos como CHAR(n) y los devuelve rellenos de espacios, por eso el trim().

fun ArticleInfo.toPostItem(
    creator: UserDtoGeneric?,
    reviews: List<ReviewDto>,
    usersById: Map<String, UserDtoGeneric>
): PostItem {
    val creatorProfile = creator?.toUserProfileInfo()
    return PostItem(
        id = id,
        creatorName = creatorProfile?.username?.trim() ?: DELETED_USER,
        timeAgo = timeAgo(createdAt),
        avatarUrl = creatorProfile?.profileImageUrl ?: "",
        commentsCount = reviews.size.toString(),
        tags = emptyList(),
        product = withReviews(reviews, usersById)
    )
}

fun ArticleInfo.withReviews(reviews: List<ReviewDto>, usersById: Map<String, UserDtoGeneric>): ArticleInfo =
    copy(
        averageRating = calculateAverageRating(reviews),
        reviewsCountText = reviewsCountText(reviews.size),
        reviewsList = reviews.map { it.toReviewData(usersById[it.userId.toString()]) }
    )

fun ReviewDto.toReviewData(author: UserDtoGeneric?): ReviewData {
    val authorProfile = author?.toUserProfileInfo()
    return ReviewData(
        id = reviewId.toString(),
        authorName = authorProfile?.username?.trim() ?: DELETED_USER,
        timeAgo = timeAgo(createdAt),
        avatarRes = R.drawable.profile, // se muestra si el autor no tiene foto
        avatarUrl = authorProfile?.profileImageUrl,
        rating = rating.toFloat(),
        reviewText = text?.trim() ?: ""
    )
}

private const val DELETED_USER = "Usuario eliminado"

// PRENDA -> "Prenda", igual que la etiqueta que ya mostraba la app
fun typeLabel(typeArticle: String): String =
    typeArticle.trim().lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }

// 129.9 -> "$129,90"
fun formatPrice(price: Double): String =
    "$" + String.format(Locale.getDefault(), "%,.2f", price)

private fun calculateAverageRating(reviews: List<ReviewDto>): Float {
    if (reviews.isEmpty()) return 0f
    val average = reviews.map { it.rating }.average()
    return Math.round(average * 10) / 10f // un decimal: 4.5
}

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
