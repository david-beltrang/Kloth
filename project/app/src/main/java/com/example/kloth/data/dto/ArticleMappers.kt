package com.example.kloth.data.dto

import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ProductDetailData
import com.example.kloth.data.local.ReviewData
import kotlin.math.roundToInt

private val categoryTagByType = mapOf(
    "PRENDA" to "Prenda",
    "OUTFIT" to "Outfit",
    "MARCA" to "Marca",
    "EVENTO" to "Evento"
)

private fun String.toCategoryLabel(): String =
    lowercase().replaceFirstChar { it.uppercaseChar() }

private fun buildReviewsCountText(count: Int): String =
    "($count ${if (count == 1) "reseña" else "reseñas"})"

private fun buildAverageRating(reviews: List<ReviewData>): Float {
    if (reviews.isEmpty()) return 0f
    val average = reviews.map { it.rating }.average()
    return (average * 10).roundToInt() / 10f
}

/**
 * Artículo -> modelo visual del Detail y de Explore.
 *
 * `brand`, `price`, `colorName` y `categoryName` solo existen para las prendas;
 * en el resto de tipos quedan en null para que la UI los oculte.
 * El `detail` completo solo llega desde `GET /articles/:id/detail`.
 */
fun ArticleDto.toProductDetailData(reviews: List<ReviewData> = emptyList()): ProductDetailData {
    val detail = this.detail
    val isPrenda = typeArticle == "PRENDA"

    return ProductDetailData(
        id = articleId.toString(),
        title = title.trim(),
        brand = if (isPrenda) detail?.brand?.trim() else null,
        price = if (isPrenda) detail?.price?.toPriceLabel() else null,
        categoryTag = categoryTagByType[typeArticle] ?: typeArticle,
        colorName = if (isPrenda) detail?.color?.trim() else null,
        categoryName = if (isPrenda) detail?.category?.trim()?.toCategoryLabel() else null,
        description = description.trim(),
        imageRes = null,
        imageUrl = imageArticle.trim().takeIf { it.isNotEmpty() },
        averageRating = buildAverageRating(reviews),
        reviewsCountText = buildReviewsCountText(reviews.size),
        isFavorite = false,
        reviewsList = reviews
    )
}

/**
 * Artículo -> publicación del Feed. El autor y las reseñas llegan resueltos
 * por el repositorio (join cliente).
 */
fun ArticleDto.toPostItem(
    author: UserDto?,
    reviews: List<ReviewData> = emptyList()
): PostItem = PostItem(
    id = articleId.toString(),
    creatorName = author?.username?.trim() ?: "Usuario",
    timeAgo = createdAt?.toTimeAgo() ?: "",
    avatarUrl = author?.imageURL ?: "",
    commentsCount = "0",
    tags = tags,
    product = toProductDetailData(reviews)
)
