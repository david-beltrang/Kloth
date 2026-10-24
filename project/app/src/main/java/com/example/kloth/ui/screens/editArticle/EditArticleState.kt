package com.example.kloth.ui.screens.editArticle

import android.net.Uri
import com.example.kloth.ui.screens.createArticle.components.ArticleType

data class EditArticleState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val type: ArticleType = ArticleType.PRENDA,
    val originalPhotoUrl: String? = null,
    val photo: Uri? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val marca: String = "",
    val color: String = "",
    val precio: String = "",
    val estilo: String = "",
    val ciudad: String = "",
    val pais: String = "",
    val organizador: String = "",
    val sitioWeb: String = "",
    val paisOrigen: String = "",
    val anioFundacion: String = ""
)