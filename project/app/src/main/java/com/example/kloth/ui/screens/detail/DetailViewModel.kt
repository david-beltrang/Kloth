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

    // A diferencia de Feed, no usamos init porque dependemos de un ID que llega después
    fun loadProduct(productId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = articleRepository.getArticleDetail(productId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(product = result.getOrNull(), isLoading = false, error = null)
                }
            } else {
                _uiState.update {
                    it.copy(product = null, isLoading = false, error = result.exceptionOrNull()?.message)
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
}
