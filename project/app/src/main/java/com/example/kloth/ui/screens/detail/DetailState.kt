package com.example.kloth.ui.screens.detail

import com.example.kloth.data.local.ArticleInfo

data class DetailState(
    val product: ArticleInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUserId: String = ""
)
