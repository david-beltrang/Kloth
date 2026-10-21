package com.example.kloth.data.dto

import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.PostItem
import java.time.Duration
import java.time.Instant
import java.util.Locale

// Funciones de mapeo: traducen lo que llega del backend (DTO) a los modelos que ya usa la UI.
// Si el backend cambia un nombre de columna, solo se toca el DTO y este archivo.
// Nota: el backend guarda los textos como CHAR(n) y los devuelve rellenos de espacios, por eso el trim().

fun ArticleInfo.toPostItem(
    creator: UserDtoGeneric?,
    reviews: List<ReviewDtoGeneric>,
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

fun ArticleInfo.withReviews(reviews: List<ReviewDtoGeneric>, usersById: Map<String, UserDtoGeneric>): ArticleInfo =
    copy(
        averageRating = calculateAverageRating(reviews),
        reviewsCountText = reviewsCountText(reviews.size),
        reviewsList = reviews.map { review ->
            review.toReviewData(usersById[review.authorKey()])
        }
    )

private const val DELETED_USER = "Usuario eliminado"

// 842 -> "842", 12800 -> "12.8k", 1500000 -> "1.5m" (como en la transcripción 10 con los likes)
fun formatCount(count: Int): String = when {
    count >= 1_000_000 -> compactCount(count / 1_000_000.0) + "m"
    count >= 1_000 -> compactCount(count / 1_000.0) + "k"
    else -> count.toString()
}

// Trunca a un decimal (12.86 -> "12.8") y quita el ".0" (12.0 -> "12")
private fun compactCount(value: Double): String {
    val truncated = Math.floor(value * 10) / 10
    return if (truncated % 1.0 == 0.0) truncated.toInt().toString()
    else String.format(Locale.US, "%.1f", truncated)
}

// PRENDA -> "Prenda", igual que la etiqueta que ya mostraba la app
fun typeLabel(typeArticle: String): String =
    typeArticle.trim().lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }

// 129.9 -> "$129,90"
fun formatPrice(price: Double): String =
    "$" + String.format(Locale.getDefault(), "%,.2f", price)

private fun calculateAverageRating(reviews: List<ReviewDtoGeneric>): Float {
    if (reviews.isEmpty()) return 0f
    val ratings = reviews.map { it.rating.toFloat() }
    val average = ratings.average()
    return Math.round(average * 10) / 10f
}

private fun reviewsCountText(count: Int): String =
    if (count == 1) "(1 reseña)" else "($count reseñas)"

// "2026-09-20T00:00:00.000Z" -> "Hace 17 días"
fun timeAgo(createdAt: String?): String {
    if (createdAt.isNullOrBlank()) return ""
    return try {
        val minutes = Duration.between(Instant.parse(createdAt), Instant.now()).toMinutes()
        when {
            minutes < 1 -> "Hace un momento"
            minutes < 60 -> "Hace $minutes min"
            minutes < 24 * 60 -> "Hace ${minutes / 60} h"
            minutes < 48 * 60 -> "Hace 1 día"
            minutes < 30 * 24 * 60 -> "Hace ${minutes / (24 * 60)} días"
            minutes < 60 * 24 * 30 -> "Hace 1 mes"
            else -> "Hace ${minutes / (24 * 60 * 30)} meses"
        }
    } catch (e: Exception) {
        ""
    }
}
