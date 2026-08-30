package com.example.kloth.ui.screens.feed

import androidx.lifecycle.ViewModel
import com.example.kloth.data.FakeArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState

    init {
        getAllPosts()
    }

    // El ViewModel es el único que habla con el Modelo (FakeArticle)
    private fun getAllPosts() {
        _uiState.update { it.copy(isLoading = true) }
        
        val allPosts = FakeArticle.posts
        
        _uiState.update { 
            it.copy(
                posts = allPosts,
                isLoading = false
            ) 
        }
    }

    // Lógica para manejar eventos de click - siguiendo o para ti
    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }
}
