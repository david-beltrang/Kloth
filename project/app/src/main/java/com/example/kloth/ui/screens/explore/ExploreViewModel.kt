package com.example.kloth.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
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

    private var loadJob: Job? = null

    fun getAllPosts() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val result = articleRepository.getFeedPosts()
            if (result.isSuccess) {
                val posts = result.getOrNull().orEmpty()
                _uiState.update {
                    it.copy(
                        allPosts = posts,
                        filteredPosts = filterPosts(posts, it.selectedCategoryIndex),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

    fun onCategorySelected(index: Int) {
        _uiState.update { state ->
            state.copy(
                selectedCategoryIndex = index,
                filteredPosts = filterPosts(state.allPosts, index)
            )
        }
    }

    private fun filterPosts(posts: List<PostItem>, index: Int): List<PostItem> {
        return when (index) {
            0 -> posts.filter { it.product.categoryName.contains("Prenda", ignoreCase = true) || it.product.categoryTag.contains("Prenda", ignoreCase = true) }
            1 -> posts.filter { it.product.categoryName.contains("Outfit", ignoreCase = true) || it.product.categoryTag.contains("Outfit", ignoreCase = true) }
            2 -> posts.filter { it.product.categoryName.contains("Marca", ignoreCase = true) || it.product.categoryTag.contains("Marca", ignoreCase = true) }
            3 -> posts.filter { it.product.categoryName.contains("Evento", ignoreCase = true) || it.product.categoryTag.contains("Evento", ignoreCase = true) }
            else -> posts
        }
    }
}
