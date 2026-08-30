package com.example.kloth.ui.screens.detail

import com.example.kloth.data.ProductDetailData

data class DetailState(
    val product: ProductDetailData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
