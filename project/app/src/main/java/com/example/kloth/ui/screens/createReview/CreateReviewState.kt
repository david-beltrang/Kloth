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
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    // La ubicación es parte del flujo de reviews para que puedan aparecer en el mapa
    val includeLocation: Boolean = false,
    val locationPermissionDenied: Boolean = false
) {
    val isFormValid: Boolean
        get() = rating > 0 && reviewText.isNotBlank() && !isLoading
}
