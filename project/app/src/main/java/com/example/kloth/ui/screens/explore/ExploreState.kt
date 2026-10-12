package com.example.kloth.ui.screens.explore

import com.example.kloth.data.local.ProductDetailData

data class ExploreState(
    val allProducts: List<ProductDetailData> = emptyList(),
    val products: List<ProductDetailData> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
