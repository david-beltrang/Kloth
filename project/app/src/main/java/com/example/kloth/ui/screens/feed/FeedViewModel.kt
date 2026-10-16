package com.example.kloth.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState

    //Usamos init para cargar todos los posts, esta funcion de getAllPosts no recibe parametros
    init {
        getAllPosts()
    }

    // Carga en curso: si se pide de nuevo (reintentar o permiso recien aceptado) se cancela la anterior
    private var loadJob: Job? = null

    // Pide los articulos al backend; el ViewModel solo mira el Result
    fun getAllPosts() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val result = articleRepository.getFeedPosts()
            ensureActive() // si esta carga se cancelo, no se toca el estado
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        posts = result.getOrNull() ?: emptyList(),
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

    // Lógica para manejar eventos de click - siguiendo o para ti
    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }
}
