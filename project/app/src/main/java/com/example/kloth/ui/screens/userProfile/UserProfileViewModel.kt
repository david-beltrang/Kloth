package com.example.kloth.ui.screens.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileState())
    val uiState: StateFlow<UserProfileState> = _uiState

    init {
        _uiState.update { it.copy(currentUserId = authRepository.currentUser?.uid.orEmpty()) }
    }

    fun getUserProfile(userId: String) {
        _uiState.update { it.copy(isLoading = true, userNotFound = false) }

        viewModelScope.launch {
            val result = userRepository.getUserById(userId, _uiState.value.currentUserId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(user = result.getOrNull(), isLoading = false)
                }
            } else {
                // Por ejemplo, un usuario eliminado: su documento ya no existe
                _uiState.update {
                    it.copy(user = null, isLoading = false, userNotFound = true)
                }
            }
        }
    }

    fun followOrUnfollowUser(targetUserId: String) {
        val currentUserId = _uiState.value.currentUserId

        viewModelScope.launch {
            val result = userRepository.followOrUnfollow(currentUserId, targetUserId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        user = it.user?.copy(
                            followers = if (it.user.followed) it.user.followers - 1 else it.user.followers + 1,
                            followed = !it.user.followed
                        )
                    )
                }
            }
        }
    }

    fun getUserArticles(userId: String) {
        viewModelScope.launch {
            articleRepository.getFeedPosts().onSuccess { posts ->
                _uiState.update {
                    it.copy(userArticles = posts.filter { post -> post.product.creatorId == userId })
                }
            }
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }
}
