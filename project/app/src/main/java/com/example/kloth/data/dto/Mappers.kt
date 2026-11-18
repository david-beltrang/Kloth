package com.example.kloth.data.dto

import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.PostItem
import java.time.Instant
import java.text.NumberFormat
import java.util.Locale
import android.text.format.DateUtils

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
        creatorId = creatorId,
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
        reviewsCount = reviews.size,
        reviewsList = reviews.map { review ->
            review.toReviewData(usersById[review.authorIdKey()])
        }
    )

private const val DELETED_USER = "Usuario eliminado"

// PRENDA -> "Prenda", igual que la etiqueta que ya mostraba la app
fun typeLabel(typeArticle: String): String =
    typeArticle.trim().lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }

// Conserva el símbolo de Kloth y localiza solo los separadores numéricos.
fun formatPrice(
    price: Double,
    currencySymbol: String = "$",
    locale: Locale = Locale.getDefault()
): String = currencySymbol + NumberFormat.getNumberInstance(locale).format(price)

private fun calculateAverageRating(reviews: List<ReviewDtoGeneric>): Float {
    if (reviews.isEmpty()) return 0f
    val ratings = reviews.map { it.rating.toFloat() }
    val average = ratings.average()
    return Math.round(average * 10) / 10f
}

// "2026-09-20T00:00:00.000Z" -> "Hace 17 días"
fun timeAgo(createdAt: String?): String {
    if (createdAt.isNullOrBlank()) return ""
    return try {
        DateUtils.getRelativeTimeSpanString(
            Instant.parse(createdAt).toEpochMilli(),
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    } catch (_: Exception) {
        ""
    }
}
