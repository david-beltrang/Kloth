package com.example.kloth.data.local

import androidx.annotation.DrawableRes

data class ArticleInfo(
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
    val reviewsList: List<ReviewData> = ResenasDataLocal.defaultReviewsList,

    val tags: List<String> = emptyList(),
    val createdAt: String? = null,
    val creatorId: String = "",
    val eliminated: Boolean = false,

    // Nuevos: datos propios de cada tipo. Solo se llenan los del tipo del artículo
    val size: String? = null,            // PRENDA
    val material: String? = null,        // PRENDA
    val style: String? = null,           // OUTFIT
    val website: String? = null,         // MARCA
    val originCountry: String? = null,   // MARCA
    val foundedAt: Int? = null,          // MARCA
    val city: String? = null,            // EVENTO
    val country: String? = null,         // EVENTO
    val startDate: String? = null,       // EVENTO
    val endDate: String? = null,         // EVENTO
    val organizer: String? = null        // EVENTO
)