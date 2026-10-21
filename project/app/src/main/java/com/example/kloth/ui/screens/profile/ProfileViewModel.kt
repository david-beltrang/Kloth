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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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

    init {
        loadUserArticles()
        viewModelScope.launch {
            articleRepository.articlesChanged.collect{ loadUserArticles()}
        }
    }

    // Se llama desde el LaunchedEffect de la pantalla y no desde init, para que los contadores
    // de seguidores y seguidos se actualicen cada vez que se vuelve al perfil
    fun loadUserProfile() {
        viewModelScope.launch {
            val userId = authRepository.currentUser?.uid ?: return@launch

            userRepository.getUserById(userId).onSuccess { user ->
                _uiState.update {
                    it.copy(
                        username = user.username?.takeIf { name -> name.isNotBlank() },
                        bio = user.bio,
                        location = user.location,
                        website = user.website,
                        email = user.email.ifBlank { it.email },
                        profileImageUrl = user.profileImageUrl?.takeIf { url -> url.isNotBlank() }
                            ?: it.profileImageUrl,
                        followersCount = user.followers,
                        followingCount = user.following
                    )
                }

                val authPhoto = authRepository.currentUser?.photoUrl?.toString()
                if (user.profileImageUrl.isNullOrBlank() && !authPhoto.isNullOrBlank()){
                    userRepository.updateProfileImage(userId,authPhoto)
                }
            }
        }
    }

    private fun loadUserArticles() {
        viewModelScope.launch {
            val userId = authRepository.currentUser?.uid ?: return@launch

            articleRepository.getFeedPosts().onSuccess { posts ->
                // 1. Artículos creados por el usuario
                val myPosts = posts.filter { it.product.creatorId == userId }

                // 2. Artículos en los que el usuario ha escrito una reseña
                //val reviewedPosts = posts.filter { post ->
                //    post.reviews.any { review -> review.userId == userId }
                //}
                _uiState.update {
                    it.copy(
                        userArticles = myPosts,
                       // reviewedArticles = reviewedPosts
                    ) }
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
                .onFailure { Log.e("ProfileViewModel", "No se pudo eliminar el articulo", it) }
            // La lista se recarga sola: ArticleRepository avisa con articlesChanged
        }
    }

}
