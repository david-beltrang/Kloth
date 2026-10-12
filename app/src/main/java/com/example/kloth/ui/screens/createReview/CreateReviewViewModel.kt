package com.example.kloth.ui.screens.createReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.Session
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.UpdateReviewDto
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private var reviewId: String? = null

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    fun loadExistingReview(reviewId: String) {
        this.reviewId = reviewId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = reviewRepository.getReviewById(reviewId)
            if (result.isSuccess) {
                val review = result.getOrNull()
                review?.let {
                    _uiState.update { state ->
                        state.copy(
                            productId = it.productId,
                            rating = it.rating.toInt(),
                            reviewText = it.reviewText,
                            isLoading = false
                        )
                    }
                    loadProductData(review.productId)
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun loadProductData(id: String) {
        if (_uiState.value.productId == id && _uiState.value.productTitle.isNotBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = articleRepository.getArticleDetail(id)

            if (result.isSuccess) {
                val product = result.getOrNull()
                _uiState.update { currentState ->
                    currentState.copy(
                        productId = id,
                        productTitle = product?.title ?: currentState.productTitle,
                        productBrand = product?.brand ?: "",
                        productVariant = product?.colorName ?: "",
                        productPrice = product?.price ?: "",
                        productImageRes = product?.imageRes,
                        productImageUrl = product?.imageUrl,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
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

    fun submitReview(onSuccess: () -> Unit = {}) {
        val state = _uiState.value
        if (!state.isFormValid) return
        val articleId = state.productId.toIntOrNull() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val editingReviewId = reviewId
            val result = if (editingReviewId != null) {
                reviewRepository.updateReview(
                    editingReviewId,
                    UpdateReviewDto(
                        rating = state.rating,
                        text = state.reviewText.trim()
                    )
                )
            } else {
                reviewRepository.createReview(
                    CreateReviewDto(
                        userId = Session.CURRENT_USER_ID,
                        articleId = articleId,
                        rating = state.rating,
                        text = state.reviewText.trim()
                    )
                )
            }

            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}
