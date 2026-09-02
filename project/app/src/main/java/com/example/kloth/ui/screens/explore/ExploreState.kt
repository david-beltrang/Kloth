package com.example.kloth.ui.screens.explore

import com.example.kloth.data.ProductDetailData

data class ExploreState(
    val products: List<ProductDetailData> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val isLoading: Boolean = false
)
