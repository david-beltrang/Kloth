package com.example.kloth.data

/**
 * Representa una publicación en el Feed social.
 * Vincula la información del creador y el contexto social con un producto concreto.
 */
data class PostItem(
    val id: String,
    val creatorName: String,
    val timeAgo: String,
    val avatarUrl: String,
    val commentsCount: String,
    val tags: List<String>,
    val product: ProductDetailData
)
