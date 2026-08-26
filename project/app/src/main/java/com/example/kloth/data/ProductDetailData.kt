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
    @DrawableRes val imageRes: Int,
    val averageRating: Float = 4.9f,
    val reviewsCountText: String = "(124 reseñas)",
    val isFavorite: Boolean = false,
    val reviewsList: List<ReviewData> = ResenasDataLocal.defaultReviewsList
)

object ProductosDataLocal {
    val sampleProductDetail = ProductDetailData(
        id = "abrigo_negro",
        title = "Abrigo de Cachemira \"Nebulosa\"",
        brand = "Maison de l'Éther",
        price = "2.450€",
        categoryTag = "Prenda",
        colorName = "Nebulosity Dark",
        categoryName = "Outerwear Elite",
        description = "Una pieza maestra de sastrería contemporánea diseñada para ofrecer máxima elegancia y confort térmico.",
        imageRes = R.drawable.abrigo_negro
    )
}