package com.example.kloth.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Colors
val KlothBlack = Color(0xFF020202)
val KlothWhite = Color(0xFFFFFFFF)
val KlothSurface = Color(0xFFFCF8F7)
val KlothOnSurface = Color(0xFF1C1B1B)
val KlothOnSurfaceVariant = Color(0xFF454743)
val DarkSurfaceContainerLow = Color(0xFF181818)

// Brand & Accent Colors
val RedInferno = Color(0xFF790005)
val AstronomicGrey = Color(0xFF8B8B8B)

// Secondary & Interaction Colors
val KlothSecondary = Color(0xFF52606C)
val KlothSecondaryContainer = Color(0xFFD3E1F0)
val KlothOnSecondaryContainer = Color(0xFF566471)

val KlothOutlineVariant = Color(0xFFC6C7C1)

// Surface Containers
val SurfaceContainerLow = Color(0xFFF7F3F2)

// Transparent Colors
val TransparentBlack30 = Color(0x4D000000)

//Color rating estrellas
val GoldStar = Color(0xFFFFB800)
val StarYellow = Color(0xFFFFB800)
val Sunset = Color(0xFFFF5C00)

// Category chip colors
val OutfitCategory = Color(0xFF6E7C89)
val MarcaCategory = Color(0xFF4E0000)
val EventoCategory = Color(0xFF142161)
val PrendaCategory = Color(0xFF8B8B8B)

fun articleCategoryColor(category: String): Color? = when (category.trim().lowercase()) {
    "outfit" -> OutfitCategory
    "marca" -> MarcaCategory
    "evento" -> EventoCategory
    "prenda" -> PrendaCategory
    else -> null
}
