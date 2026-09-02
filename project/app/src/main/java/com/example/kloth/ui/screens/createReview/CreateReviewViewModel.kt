package com.example.kloth.ui.screens.createReview

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kloth.data.FakeArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateReviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    fun loadProductData(id: String) {
        val product = FakeArticle.obtenerProductoPorId(id)

        if (product != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    productId = id,
                    productTitle = product.title,
                    productBrand = product.brand,
                    productVariant = product.colorName,
                    productPrice = product.price,
                    productImageRes = product.imageRes,
                    productImageUrl = product.imageUrl
                )
            }
            Log.d("CreateReviewVM", "Producto cargado exitosamente: ${product.title}")
        } else {
            Log.e("CreateReviewVM", "No se encontró ningún producto en FakeArticle con el ID: '$id'")
        }
    }

    fun onRatingSelected(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun onReviewTextChanged(text: String) {
        if (text.length <= _uiState.value.maxTextLength) {
            _uiState.update { it.copy(reviewText = text) }
        }
    }

    fun submitReview(onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true) }
        // Aquí iría la lógica de persistencia
        _uiState.update { it.copy(isLoading = false) }
        onSuccess()
    }
}
