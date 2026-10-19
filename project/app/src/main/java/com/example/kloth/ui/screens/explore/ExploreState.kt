package com.example.kloth.ui.screens.explore

import com.example.kloth.data.local.ArticleInfo
data class ExploreState(
    val products: List<ArticleInfo> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val isLoading: Boolean = false
)
