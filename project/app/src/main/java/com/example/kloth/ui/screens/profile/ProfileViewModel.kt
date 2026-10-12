package com.example.kloth.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.Session
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.ReviewRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.ui.navigation.RouteConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val articleRepository: ArticleRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val profileUserId: Int =
        savedStateHandle.get<String>(RouteConstants.USER_PROFILE_ARG_ID)?.toIntOrNull()
            ?: Session.CURRENT_USER_ID

    private val isCurrentUser: Boolean = profileUserId == Session.CURRENT_USER_ID

    private val _uiState = MutableStateFlow(ProfileState(
        email = if (isCurrentUser) authRepository.currentUser?.email ?: "" else "",
        profileImageUrl = if (isCurrentUser) authRepository.currentUser?.photoUrl?.toString() else null,
        isCurrentUser = isCurrentUser
    ))

    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    // La pantalla decide cuándo cargar: LaunchedEffect(Unit) en ProfileScreen.
    fun loadProfile() {
        loadProfileUser()
        loadUserArticles()
        loadUserReviews()
    }

    private fun loadProfileUser() {
        viewModelScope.launch {
            val result = userRepository.getUserById(profileUserId.toString())
            if (result.isSuccess) {
                val user = result.getOrNull()
                _uiState.update { state ->
                    state.copy(
                        // En el propio perfil se conservan foto/email de Firebase y
                        // solo se agrega el nombre real del backend.
                        email = if (isCurrentUser) state.email else (user?.email ?: "no disponible"),
                        profileImageUrl = if (isCurrentUser) state.profileImageUrl else user?.imageURL,
                        profileUserName = user?.username
                    )
                }
            }
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
        if (index == 0) {
            loadUserArticles()
        } else if (index == 1) {
            loadUserReviews()
        }
    }

    private fun loadUserArticles() {
        val userId = profileUserId.toString()
        // Si ya hay datos la recarga es silenciosa (no vuelve a mostrar el
        // spinner cuando se regresa de crear reseña o de ver un detalle).
        val silent = _uiState.value.userArticles.isNotEmpty()
        if (!silent) {
            _uiState.update { it.copy(isLoadingArticles = true) }
        }
        viewModelScope.launch {
            val result = articleRepository.getUserArticles(userId)
            if (result.isSuccess) {
                _uiState.update { it.copy(userArticles = result.getOrNull() ?: emptyList(), isLoadingArticles = false) }
            } else if (!silent) {
                _uiState.update { it.copy(isLoadingArticles = false) }
            }
        }
    }

    private fun loadUserReviews() {
        val userId = profileUserId.toString()
        val silent = _uiState.value.userReviews.isNotEmpty()
        if (!silent) {
            _uiState.update { it.copy(isLoadingReviews = true) }
        }
        viewModelScope.launch {
            val result = reviewRepository.getUserReviews(userId)
            if (result.isSuccess) {
                _uiState.update { it.copy(userReviews = result.getOrNull() ?: emptyList(), isLoadingReviews = false) }
            } else if (!silent) {
                _uiState.update { it.copy(isLoadingReviews = false) }
            }
        }
    }

    fun logOutButtonPressed() {
        authRepository.signOut()
        _uiState.update { it.copy(navigate = true) }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigate = false) }
    }

    fun uploadImageToFirebase(uri: android.net.Uri){
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess){
                _uiState.update { it.copy(profileImageUrl = result.getOrNull())}
            }
        }
    }
}