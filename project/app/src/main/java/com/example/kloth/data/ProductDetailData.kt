package com.example.kloth.data

import androidx.annotation.DrawableRes
import com.example.kloth.R

data class ProductDetailData(
    val id: String,
    val title: String,
    val brand: String,
    val price: String,
    val categoryTag: String,
    val colorName: String,
    val categoryName: String,
    val description: String,
    @get:DrawableRes val imageRes: Int? = null,
    val imageUrl: String? = null,
    val averageRating: Float = 4.9f,
    val reviewsCountText: String = "(124 reseñas)",
    val isFavorite: Boolean = false,
    val reviewsList: List<ReviewData> = ResenasDataLocal.defaultReviewsList
)
