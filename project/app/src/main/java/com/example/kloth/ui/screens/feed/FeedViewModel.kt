package com.example.kloth.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState

    // Carga en curso: si se pide de nuevo (reintentar o permiso recien aceptado) se cancela la anterior
    private var loadJob: Job? = null
    private var followingIds: Set<String> = emptySet()

    // Pide los articulos al backend; el ViewModel solo mira el Result
    fun getAllPosts() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val result = articleRepository.getFeedPosts()
            ensureActive() // si esta carga se cancelo, no se toca el estado
            if (result.isSuccess) {
                val currentUserId = authRepository.currentUser?.uid
                followingIds = currentUserId?.let {
                    userRepository.getFollowingIds(it).getOrDefault(emptySet())
                } ?: emptySet()
                _uiState.update {
                    it.copy(
                        allPosts = result.getOrNull().orEmpty(),
                        posts = filterPosts(result.getOrNull().orEmpty(), it.selectedTabIndex),
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
        if (index == 0) {
            viewModelScope.launch {
                authRepository.currentUser?.uid?.let { userId ->
                    followingIds = userRepository.getFollowingIds(userId).getOrDefault(emptySet())
                }
                _uiState.update { state ->
                    state.copy(selectedTabIndex = index, posts = filterPosts(state.allPosts, index))
                }
            }
            return
        }
        _uiState.update { state ->
            state.copy(selectedTabIndex = index, posts = filterPosts(state.allPosts, index))
        }
    }

    private fun filterPosts(posts: List<PostItem>, index: Int) =
        if (index == 0) posts.filter { it.creatorId in followingIds } else posts
}
