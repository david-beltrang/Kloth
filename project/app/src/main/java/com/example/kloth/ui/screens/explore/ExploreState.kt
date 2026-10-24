package com.example.kloth.ui.screens.explore

import com.example.kloth.data.local.ArticleInfo
import com.example.kloth.data.local.PostItem

data class ExploreState(
    val allPosts: List<PostItem> = emptyList(),
    val filteredPosts: List<PostItem> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
