package com.example.kloth.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.local.ProductDetailData
import com.example.kloth.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreState())
    val uiState: StateFlow<ExploreState> = _uiState

    private val categories = listOf("Prenda", "Outfit", "Marca", "Evento")

    fun loadProducts() {
        viewModelScope.launch {
            val silent = _uiState.value.allProducts.isNotEmpty()
            if (!silent) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            val result = articleRepository.getFeedPosts()

            if (result.isSuccess) {
                val all = result.getOrNull().orEmpty().map { it.product }
                _uiState.update { state ->
                    state.copy(
                        allProducts = all,
                        products = filterByCategory(all, state.selectedCategoryIndex),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else if (!silent) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun retry() {
        loadProducts()
    }

    fun onCategorySelected(index: Int) {
        _uiState.update { state ->
            state.copy(
                selectedCategoryIndex = index,
                products = filterByCategory(state.allProducts, index)
            )
        }
    }

    private fun filterByCategory(products: List<ProductDetailData>, index: Int): List<ProductDetailData> {
        val category = categories.getOrNull(index) ?: return products
        return products.filter { it.categoryTag == category }
    }
}