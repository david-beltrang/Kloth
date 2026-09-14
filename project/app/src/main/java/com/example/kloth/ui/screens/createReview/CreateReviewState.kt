package com.example.kloth.ui.screens.createReview

data class CreateReviewState(
    val productId: String = "",
    val productTitle: String = "",
    val productBrand: String = "",
    val productVariant: String = "",
    val productPrice: String = "",
    val productImageRes: Int? = null,
    val productImageUrl: String? = null,
    val rating: Int = 0,
    val reviewText: String = "",
    val maxTextLength: Int = 500,
    val isLoading: Boolean = false
) {
    val isFormValid: Boolean
        get() = rating > 0 && reviewText.isNotBlank() && !isLoading
}