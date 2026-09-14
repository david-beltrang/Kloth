package com.example.kloth.ui.screens.createArticle

import com.example.kloth.R
import com.example.kloth.ui.screens.createArticle.components.ArticleType
data class CreateArticleState(
    val selectedType: ArticleType = ArticleType.PRENDA,
    val photos: List<Int> = listOf(R.drawable.abrigo_negro, R.drawable.bolso_cuero),
    val nombre: String = "",
    val descripcion: String = "",
    val marca: String = "",
    val color: String = "",
    val precio: String = "",
    val estilo: String = "",
    val ciudad: String = "",
    val pais: String = "",
    val organizador: String = "",
    val isPublishing: Boolean = false
)