package com.example.kloth.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreState())
    val uiState: StateFlow<ExploreState> = _uiState

    private var loadJob: Job? = null
    private var searchJob: Job? = null

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
                        filteredPosts = ExplorePostSearch.filterPosts(posts, it.query, it.selectedCategoryIndex),
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
                filteredPosts = ExplorePostSearch.filterPosts(state.allPosts, state.query, index)
            )
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            _uiState.update { state ->
                state.copy(filteredPosts = ExplorePostSearch.filterPosts(state.allPosts, state.query, state.selectedCategoryIndex))
            }
        }
    }

    fun onClearQuery() = onQueryChange("")
}
