package com.example.kloth.ui.screens.createArticle

import android.net.Uri
import com.example.kloth.R
import com.example.kloth.ui.screens.createArticle.components.ArticleType
data class CreateArticleState(
    val selectedType: ArticleType = ArticleType.PRENDA,
    val photos: List<Uri> = emptyList(),
    val nombre: String = "",
    val descripcion: String = "",
    val marca: String = "",
    val color: String = "",
    val precio: String = "",
    val estilo: String = "",
    val ciudad: String = "",
    val pais: String = "",
    val fechaInicio: String = "",
    val fechaFin: String = "",
    val organizador: String = "",
    val isPublishing: Boolean = false,
    val sitioWeb: String = "",
    val paisOrigen: String = "",
    val anioFundacion: String = "",

    )
