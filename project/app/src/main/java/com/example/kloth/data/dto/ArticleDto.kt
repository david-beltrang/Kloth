package com.example.kloth.data.dto

import com.example.kloth.data.local.ArticleInfo

abstract class ArticleDtoGeneric {
    abstract fun toArticleInfo(): ArticleInfo
}

data class ArticleRetrofitDto(
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
) : ArticleDtoGeneric() {

    override fun toArticleInfo(): ArticleInfo {
        return ArticleInfo(
            id = articleId.toString(),
            title = title.trim(),
            brand = detail?.brand.orEmpty(),
            price = detail?.price?.let { formatPrice(it) }.orEmpty(),
            categoryTag = typeLabel(typeArticle),
            colorName = detail?.color.orEmpty(),
            categoryName = detail?.category.orEmpty(),
            description = description.trim(),
            imageUrl = imageArticle.trim().takeIf { it.isNotEmpty() },
            tags = tags,
            createdAt = createdAt,
            creatorId = idUser.toString(),
            eliminated = eliminated,
            size = detail?.size,
            material = detail?.material,
            style = detail?.style,
            website = detail?.website,
            originCountry = detail?.originCountry,
            foundedAt = detail?.foundedAt,
            city = detail?.city,
            country = detail?.country,
            startDate = detail?.startDate,
            endDate = detail?.endDate,
            organizer = detail?.organizer
        )
    }
}

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


data class CreateArticleDto(
    val typeArticle: String,
    val title: String,
    val description: String,
    val idUser: String,
    val imageArticle: String,
    val createdAt: String,
    val eliminated: Boolean = false,
    val tags: List<String> = emptyList(),
    // Solo se llenan los del tipo del artículo
    val brand: String? = null,
    val color: String? = null,
    val price: Double? = null,
    val style: String? = null,
    val city: String? = null,
    val country: String? = null,
    val organizer: String? = null,
    val website: String? = null,
    val originCountry: String? = null,
    val foundedAt: Int? = null
)

data class ArticleFirestoreDto(
    val articleId: String = "",
    val typeArticle: String = "",
    val title: String = "",
    val description: String = "",
    val createdAt: String? = null,
    val idUser: String = "",
    val eliminated: Boolean = false,
    val imageArticle: String = "",
    val updatedAt: String? = null,
    val tags: List<String> = emptyList(),
    // PRENDA
    val size: String? = null,
    val color: String? = null,
    val category: String? = null,
    val material: String? = null,
    val brand: String? = null,
    val price: Double? = null,
    // OUTFIT
    val style: String? = null,
    // MARCA
    val website: String? = null,
    val originCountry: String? = null,
    val foundedAt: Int? = null,
    // EVENTO
    val city: String? = null,
    val country: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val organizer: String? = null
) : ArticleDtoGeneric() {

    override fun toArticleInfo(): ArticleInfo {
        return ArticleInfo(
            id = articleId,
            title = title.trim(),
            brand = brand.orEmpty(),
            price = price?.let { formatPrice(it) }.orEmpty(),
            categoryTag = typeLabel(typeArticle),
            colorName = color.orEmpty(),
            categoryName = category.orEmpty(),
            description = description.trim(),
            imageUrl = imageArticle.trim().takeIf { it.isNotEmpty() },
            tags = tags,
            createdAt = createdAt,
            creatorId = idUser,
            eliminated = eliminated,
            size = size,
            material = material,
            style = style,
            website = website,
            originCountry = originCountry,
            foundedAt = foundedAt,
            city = city,
            country = country,
            startDate = startDate,
            endDate = endDate,
            organizer = organizer
        )
    }
}