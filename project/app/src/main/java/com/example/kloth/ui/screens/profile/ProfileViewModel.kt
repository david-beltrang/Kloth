package com.example.kloth.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState(
        email = authRepository.currentUser?.email ?: "",
        profileImageUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
    ))

    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    // Se llama desde la pantalla con LaunchedEffect(Unit) para recargar al volver
    fun refreshProfile(profileUserId: String? = null) {
        val userId = profileUserId?.takeIf { it.isNotBlank() } ?: authRepository.currentUser?.uid ?: return
        val isOwnProfile = userId == authRepository.currentUser?.uid
        _uiState.update {
            it.copy(
                viewingOwnProfile = isOwnProfile,
                targetUserId = userId,
                isFollowing = false,
                isFollowActionInProgress = false
            )
        }
        loadUserProfile(userId, isOwnProfile)
        loadUserArticles(userId, isOwnProfile)
        if (!isOwnProfile) loadFollowStatus(userId)
    }

    private fun loadUserProfile(userId: String, isOwnProfile: Boolean) {
        viewModelScope.launch {
            userRepository.getUserById(userId).onSuccess { user ->
                _uiState.update {
                    it.copy(
                        username = user.username?.takeIf { name -> name.isNotBlank() },
                        bio = user.bio,
                        location = user.location,
                        website = user.website,
                        followersCount = user.followersCount,
                        followingCount = user.followingCount,
                        email = user.email.ifBlank { it.email },
                        profileImageUrl = user.profileImageUrl?.takeIf { url -> url.isNotBlank() }
                            ?: if (isOwnProfile) it.profileImageUrl else null
                    )
                }

                val authPhoto = authRepository.currentUser?.photoUrl?.toString()
                if (isOwnProfile && user.profileImageUrl.isNullOrBlank() && !authPhoto.isNullOrBlank()){
                    userRepository.updateProfileImage(userId,authPhoto)
                }
            }
        }
    }

    private fun loadFollowStatus(targetUserId: String) {
        val currentUserId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            userRepository.isFollowing(currentUserId, targetUserId).onSuccess { isFollowing ->
                _uiState.update { it.copy(isFollowing = isFollowing) }
            }
        }
    }

    fun toggleFollow() {
        val currentUserId = authRepository.currentUser?.uid ?: return
        val currentState = _uiState.value
        val targetUserId = currentState.targetUserId ?: return
        if (currentState.viewingOwnProfile || currentState.isFollowActionInProgress) return

        viewModelScope.launch {
            _uiState.update { it.copy(isFollowActionInProgress = true) }
            userRepository.followOrUnfollowUser(currentUserId, targetUserId)
                .onSuccess { followState ->
                    _uiState.update { state ->
                        state.copy(
                            isFollowing = followState.isFollowing,
                            followersCount = followState.followersCount
                        )
                    }
                }
            _uiState.update { it.copy(isFollowActionInProgress = false) }
        }
    }

    private fun loadUserArticles(userId: String, isOwnProfile: Boolean) {
        viewModelScope.launch {
            articleRepository.getFeedPosts().onSuccess { posts ->
                // 1. Artículos creados por el usuario
                val myPosts = posts.filter { it.product.creatorId == userId }

                // 2. Artículos en los que el usuario ha escrito una reseña
                val reviewedPosts = posts.filter { post ->
                    post.product.reviewsList.any { review -> review.authorId == userId }
                }
                _uiState.update {
                    it.copy(
                        userArticles = myPosts,
                        reviewedArticles = reviewedPosts,
                        viewingOwnProfile = isOwnProfile
                    )
                }
            }
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun logOutButtonPressed() {
        authRepository.signOut()
        _uiState.update { it.copy(navigate = true) }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigate = false) }
    }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            val userId = authRepository.currentUser?.uid ?: return@launch
            storageRepository.uploadProfileImage(uri).onSuccess { url ->
                _uiState.update { it.copy(profileImageUrl = url) }
                userRepository.updateProfileImage(userId, url)   // sincroniza el documento del usuario
            }
        }
    }

    fun deleteArticle(post: PostItem) {
        viewModelScope.launch {
            articleRepository.deleteArticle(post.product.id, post.product.imageUrl)
                .onSuccess { refreshProfile() }
                .onFailure { Log.e("ProfileViewModel", "No se pudo eliminar el articulo", it) }
        }
    }

}
