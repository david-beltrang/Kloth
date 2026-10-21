package com.example.kloth.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailState())
    val uiState: StateFlow<DetailState> = _uiState

    init {
        _uiState.update { it.copy(currentUserId = authRepository.currentUser?.uid.orEmpty()) }
    }

    // A diferencia de Feed, no usamos init porque dependemos de un ID que llega después
    fun loadProduct(productId: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = articleRepository.getArticleDetail(productId, _uiState.value.currentUserId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(product = result.getOrNull(), isLoading = false, errorMessage = null)
                }
            } else {
                _uiState.update {
                    it.copy(product = null, isLoading = false, errorMessage = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun toggleFavorite() {
        _uiState.update { currentState ->
            currentState.product?.let { currentProduct ->
                val updatedProduct = currentProduct.copy(isFavorite = !currentProduct.isFavorite)
                currentState.copy(product = updatedProduct)
            } ?: currentState
        }
    }

    fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewLike(reviewId, userId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        product = it.product?.copy(
                            reviewsList = it.product.reviewsList.map { review ->
                                if (review.id == reviewId) {
                                    review.copy(
                                        liked = !review.liked,
                                        likesCount = if (review.liked) review.likesCount - 1 else review.likesCount + 1
                                    )
                                } else review
                            }
                        )
                    )
                }
            }
        }
    }
}
