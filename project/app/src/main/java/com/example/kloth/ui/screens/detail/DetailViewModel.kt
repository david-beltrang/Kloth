package com.example.kloth.ui.screens.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.TranslationOutcome
import com.example.kloth.data.repository.TranslationRepository
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import com.example.kloth.ui.utils.TranslationContentIds
import com.example.kloth.ui.utils.TranslationTextState
import com.example.kloth.ui.utils.TranslationUiStatus
import com.example.kloth.ui.utils.appTranslationLanguage

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    private val translationRepository: TranslationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailState())
    val uiState: StateFlow<DetailState> = _uiState
    private var reviewsJob: Job? = null

    // A diferencia de Feed, no usamos init porque dependemos de un ID que llega después
    fun loadProduct(productId: String) {
        reviewsJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null, translationStates = emptyMap()) }

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
                if (loadedProduct != null) {
                    observeTranslation(TranslationContentIds.description(loadedProduct.id), loadedProduct.description)
                    loadedProduct.reviewsList.forEach { review ->
                        observeTranslation(TranslationContentIds.review(review.id), review.reviewText)
                    }
                    observeReviews(productId, userId)
                }
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
                    withLikeStates.forEach { review ->
                        observeTranslation(TranslationContentIds.review(review.id), review.reviewText)
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

    fun onTranslate(contentId: String, originalText: String) {
        val current = _uiState.value.translationStates[contentId] ?: return
        if (!current.canTranslate) return
        updateTranslation(contentId) { it.copy(status = TranslationUiStatus.Translating) }
        viewModelScope.launch {
            try {
                when (val outcome = translationRepository.translate(
                    contentId,
                    originalText,
                    appTranslationLanguage(context)
                )) {
                    is TranslationOutcome.Translated -> updateTranslation(contentId) {
                        it.copy(status = TranslationUiStatus.Translated(outcome.text))
                    }
                    is TranslationOutcome.SameLanguage,
                    TranslationOutcome.Undetermined -> updateTranslation(contentId) {
                        it.copy(canTranslate = false, status = TranslationUiStatus.Original)
                    }
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                updateTranslation(contentId) { it.copy(status = TranslationUiStatus.Error) }
            }
        }
    }

    fun onShowOriginal(contentId: String) = updateTranslation(contentId) {
        it.copy(status = TranslationUiStatus.Original)
    }

    private fun observeTranslation(contentId: String, text: String) {
        if (text.isBlank() || contentId in _uiState.value.translationStates) return
        _uiState.update { state ->
            state.copy(translationStates = state.translationStates +
                (contentId to TranslationTextState(isDetectingLanguage = true)))
        }
        viewModelScope.launch {
            val canTranslate = runCatching {
                translationRepository.canTranslate(contentId, text, appTranslationLanguage(context))
            }.getOrDefault(false)
            updateTranslation(contentId) {
                it.copy(canTranslate = canTranslate, isDetectingLanguage = false)
            }
        }
    }

    private fun updateTranslation(
        contentId: String,
        transform: (TranslationTextState) -> TranslationTextState
    ) {
        _uiState.update { state ->
            val current = state.translationStates[contentId] ?: TranslationTextState()
            state.copy(translationStates = state.translationStates + (contentId to transform(current)))
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
