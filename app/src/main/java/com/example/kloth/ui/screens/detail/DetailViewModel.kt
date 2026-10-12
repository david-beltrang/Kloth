package com.example.kloth.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailState())
    val uiState: StateFlow<DetailState> = _uiState

    private var loadedProductId: String? = null

    fun loadProduct(productId: String) {
        if (loadedProductId == productId) {
            loadArticleReviews(productId, silent = true)
            return
        }
        loadedProductId = productId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val detailResult = articleRepository.getArticleDetail(productId)
            if (detailResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = detailResult.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    product = detailResult.getOrNull(),
                    isLoading = false
                )
            }

            loadArticleReviews(productId)
        }
    }

    private fun loadArticleReviews(productId: String, silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) {
                _uiState.update { it.copy(isLoadingReviews = true, error = null) }
            }

            val reviewsResult = articleRepository.getArticleReviews(productId)
            if (reviewsResult.isSuccess) {
                val reviews = reviewsResult.getOrNull() ?: emptyList()
                _uiState.update { state ->
                    state.copy(
                        reviews = reviews,
                        isLoadingReviews = false,
                        product = state.product?.let { product ->
                            val average = if (reviews.isEmpty()) 0f
                            else ((reviews.map { it.rating }.average()) * 10).toInt() / 10f
                            product.copy(
                                reviewsList = reviews,
                                averageRating = average,
                                reviewsCountText =
                                    "(${reviews.size} ${if (reviews.size == 1) "reseña" else "reseñas"})"
                            )
                        }
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoadingReviews = false,
                        error = reviewsResult.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun retry() {
        val id = loadedProductId ?: return
        loadedProductId = null
        loadProduct(id)
    }

    fun toggleFavorite() {
        _uiState.update { currentState ->
            currentState.product?.let { currentProduct ->
                val updatedProduct = currentProduct.copy(isFavorite = !currentProduct.isFavorite)
                currentState.copy(product = updatedProduct)
            } ?: currentState
        }
    }
}
