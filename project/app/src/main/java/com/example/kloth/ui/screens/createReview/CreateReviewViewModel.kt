package com.example.kloth.ui.screens.createReview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.LocationRepository
import com.example.kloth.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    fun loadProductData(id: String) {
        _uiState.update { it.copy(productId = id) }
        viewModelScope.launch {
            articleRepository.getArticleForEdit(id)
                .onSuccess { product ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            productTitle = product.title,
                            productBrand = product.brand,
                            productVariant = product.colorName,
                            productPrice = product.price,
                            productImageRes = product.imageRes,
                            productImageUrl = product.imageUrl
                        )
                    }
                }
                .onFailure { Log.e("CreateReviewVM", "No se pudo cargar el artículo '$id'", it) }
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

    // Cuando el usuario activa el switch SIN tener permiso aún, la pantalla lanza el launcher de permiso
    // Este método solo se llama si el permiso ya fue concedido
    fun onIncludeLocationChanged(enabled: Boolean) {
        _uiState.update { it.copy(includeLocation = enabled, locationPermissionDenied = false) }
    }

    // Resultado del launcher de permisos de ubicación
    fun onLocationPermissionResult(granted: Boolean) {
        if (granted) {
            _uiState.update { it.copy(includeLocation = true, locationPermissionDenied = false) }
        } else {
            _uiState.update { it.copy(includeLocation = false, locationPermissionDenied = true) }
        }
    }

    fun submitReview() {
        val state = _uiState.value
        val userId = authRepository.currentUser?.uid ?: return
        if (!state.isFormValid) return

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            // Captura de ubicación: Si falla o está desactivada, se publica sin coordenadas
            val coordinates = if (state.includeLocation) {
                locationRepository.getCurrentCoordinates().getOrNull()
            } else {
                null
            }
            // Solo se captura la ubicación si el usuario activó el switch
            if (state.includeLocation && coordinates == null) {
                _uiState.update {
                    it.copy(isLoading = false, locationPermissionDenied = true)
                }
                return@launch
            }

            val result = reviewRepository.createReview(
                articleId = state.productId,
                userId = userId,
                rating = state.rating,
                text = state.reviewText.trim(),
                coordinates = coordinates
            )
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSubmitted = true) }
            } else {
                Log.e("CreateReviewVM", "No se pudo guardar la reseña", result.exceptionOrNull())
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSubmittedHandled() {
        _uiState.update { it.copy(isSubmitted = false) }
    }
}
