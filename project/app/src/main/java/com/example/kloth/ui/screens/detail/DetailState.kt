package com.example.kloth.ui.screens.detail

import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.ui.utils.TranslationTextState

data class DetailState(
    val product: ArticleInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val translationStates: Map<String, TranslationTextState> = emptyMap()
)
