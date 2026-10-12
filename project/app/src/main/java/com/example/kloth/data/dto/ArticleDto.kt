package com.example.kloth.data.dto

/**
 * Estructura exacta de un artículo tal como lo devuelve el backend
 * (`GET /articles`, `GET /articles/:id`, `GET /articles/:id/detail`).
 * Los IDs se reciben como [Int] y se convierten a [String] en los mappers.
 */
data class ArticleDto(
    val articleId: Int,
    val typeArticle: String,
    val title: String,
    val description: String,
    val createdAt: String? = null,
    val idUser: Int,
    val eliminated: Boolean = false,
    val imageArticle: String,
    val updatedAt: String? = null,
    val tags: List<String> = emptyList(),
    val detail: ArticleDetailDto? = null
)

/**
 * Unión de las cuatro tablas hijas de `articles` (prendas, outfits, brands, events).
 * Solo los campos del tipo de artículo vienen rellenados; el resto queda en null.
 * Solo se envía en `GET /articles/:id/detail`.
 */
data class ArticleDetailDto(
    // PRENDA (prendas)
    val prendaId: Int? = null,
    val size: String? = null,
    val color: String? = null,
    val category: String? = null,
    val material: String? = null,
    val brand: String? = null,
    val price: Double? = null,
    // OUTFIT (outfits)
    val outfitId: Int? = null,
    val style: String? = null,
    // MARCA (brands)
    val brandId: Int? = null,
    val website: String? = null,
    val originCountry: String? = null,
    val foundedAt: Int? = null,
    // EVENTO (events)
    val eventId: Int? = null,
    val city: String? = null,
    val country: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val organizer: String? = null
)

/**
 * Cuerpo de `POST /articles`. `eliminated` y `tags` viajan siempre desde el
 * cliente porque el modelo de Sequelize no define valores por defecto.
 */
data class CreateArticleDto(
    val typeArticle: String,
    val title: String,
    val description: String,
    val idUser: Int,
    val imageArticle: String,
    val eliminated: Boolean = false,
    val tags: List<String> = emptyList()
)
