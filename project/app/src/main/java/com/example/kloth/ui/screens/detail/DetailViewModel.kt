package com.example.kloth.ui.screens.detail

import androidx.lifecycle.ViewModel
import com.example.kloth.data.FakeArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailState())
    val uiState: StateFlow<DetailState> = _uiState

    // A diferencia de Feed, no usamos init porque dependemos de un ID que llega después
    fun loadProduct(productId: String) {
        _uiState.update { it.copy(isLoading = true) }
        
        val product = FakeArticle.obtenerProductoPorId(productId)
        
        _uiState.update { 
            it.copy(
                product = product,
                isLoading = false 
            ) 
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
