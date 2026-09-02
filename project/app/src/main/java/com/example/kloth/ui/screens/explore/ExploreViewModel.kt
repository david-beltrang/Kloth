package com.example.kloth.ui.screens.explore

import androidx.lifecycle.ViewModel
import com.example.kloth.data.FakeArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ExploreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreState())
    val uiState: StateFlow<ExploreState> = _uiState

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _uiState.update { it.copy(isLoading = true) }

        val products = FakeArticle.allProducts

        _uiState.update {
            it.copy(
                products = products,
                isLoading = false
            )
        }
    }

    fun onCategorySelected(index: Int) {
        _uiState.update { it.copy(selectedCategoryIndex = index) }
    }
}
