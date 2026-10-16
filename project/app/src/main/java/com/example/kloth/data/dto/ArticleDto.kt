package com.example.kloth.data.dto

// DTO de entrada: copia exacta del JSON que devuelve GET /articles y GET /articles/{id}
data class ArticleDto(
    val articleId: Int,
    val typeArticle: String,     // PRENDA, OUTFIT, MARCA o EVENTO
    val title: String,
    val description: String,
    val createdAt: String?,
    val idUser: Int,
    val eliminated: Boolean,
    val imageArticle: String,
    val updatedAt: String?
)
