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
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailState())
    val uiState: StateFlow<DetailState> = _uiState
    private var reviewsJob: Job? = null

    // A diferencia de Feed, no usamos init porque dependemos de un ID que llega después
    fun loadProduct(productId: String) {
        reviewsJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = articleRepository.getArticleDetail(productId)
            if (result.isSuccess) {
                val userId = authRepository.currentUser?.uid
                val loadedProduct = result.getOrNull()
                val productWithLikeStates = if (loadedProduct != null && userId != null) {
                    loadedProduct.copy(
                        reviewsList = loadedProduct.reviewsList.map { review ->
                            val isLiked = reviewRepository.isReviewLikedByUser(review.id, userId)
                                .getOrDefault(false)
                            review.copy(isLikedByMe = isLiked)
                        }
                    )
                } else loadedProduct
                _uiState.update {
                    it.copy(product = productWithLikeStates, isLoading = false, errorMessage = null)
                }
                if (loadedProduct != null) observeReviews(productId, userId)
            } else {
                _uiState.update {
                    it.copy(product = null, isLoading = false, errorMessage = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    private fun observeReviews(productId: String, userId: String?) {
        reviewsJob = viewModelScope.launch {
            reviewRepository.observeReviewsForArticle(productId)
                .catch { error ->
                    if (error is CancellationException) throw error
                }
                .collect { reviews ->
                    val withLikeStates = if (userId == null) reviews else reviews.map { review ->
                        review.copy(isLikedByMe = reviewRepository
                            .isReviewLikedByUser(review.id, userId).getOrDefault(false))
                    }
                    _uiState.update { state ->
                        val product = state.product ?: return@update state
                        state.copy(product = product.copy(
                            reviewsList = withLikeStates,
                            reviewsCount = withLikeStates.size,
                            averageRating = if (withLikeStates.isEmpty()) 0f else
                                (withLikeStates.map { it.rating }.average() * 10).toInt() / 10f
                        ))
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

    fun toggleReviewLike(reviewId: String) {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            reviewRepository.toggleReviewLike(reviewId, userId).onSuccess { likeState ->
                _uiState.update { state ->
                    val product = state.product ?: return@update state
                    val reviews = product.reviewsList.map { review ->
                        if (review.id != reviewId) review else review.copy(
                            isLikedByMe = likeState.isLikedByMe,
                            likesCount = likeState.likesCount
                        )
                    }
                    state.copy(product = product.copy(reviewsList = reviews))
                }
            }
        }
    }
}
