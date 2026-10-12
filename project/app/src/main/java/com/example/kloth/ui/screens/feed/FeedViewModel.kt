package com.example.kloth.ui.screens.feed

import androidx.lifecycle.ViewModel
import com.example.kloth.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState

    fun getAllPosts() {
        viewModelScope.launch {
            val silent = _uiState.value.posts.isNotEmpty()
            if (!silent) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            val result = articleRepository.getFeedPosts()

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        posts = result.getOrNull() ?: emptyList(),
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

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }
}
