package com.example.kloth.data.local

import androidx.annotation.DrawableRes

/**
 * Modelo visual del artículo en Detail/Explore.
 * Los campos nulos no existen para el tipo de artículo y no deben pintarse en la UI.
 */
data class ProductDetailData(
    val id: String,
    val title: String,
    val brand: String? = null,
    val price: String? = null,
    val categoryTag: String,
    val colorName: String? = null,
    val categoryName: String? = null,
    val description: String,
    @get:DrawableRes val imageRes: Int? = null,
    val imageUrl: String? = null,
    val averageRating: Float = 4.9f,
    val reviewsCountText: String = "(124 reseñas)",
    val isFavorite: Boolean = false,
    val reviewsList: List<ReviewData> = ResenasDataLocal.defaultReviewsList
)
