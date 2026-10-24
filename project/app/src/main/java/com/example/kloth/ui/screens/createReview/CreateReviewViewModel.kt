package com.example.kloth.ui.screens.createReview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
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
    private val authRepository: AuthRepository
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

    fun submitReview() {
        val state = _uiState.value
        val userId = authRepository.currentUser?.uid ?: return
        if (!state.isFormValid) return

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = reviewRepository.createReview(
                articleId = state.productId,
                userId = userId,
                rating = state.rating,
                text = state.reviewText.trim()
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
