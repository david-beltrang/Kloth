package com.example.kloth.ui.screens.detail

import com.example.kloth.data.local.ProductDetailData
import com.example.kloth.data.local.ReviewData

data class DetailState(
    val product: ProductDetailData? = null,
    val reviews: List<ReviewData> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingReviews: Boolean = false,
    val error: String? = null
)
