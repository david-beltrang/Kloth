package com.example.kloth.ui.screens.reviewsMap

import com.example.kloth.data.local.ReviewMapPin

data class ReviewsMapState(
    val pins: List<ReviewMapPin> = emptyList(),
    val selectedPin: ReviewMapPin? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
