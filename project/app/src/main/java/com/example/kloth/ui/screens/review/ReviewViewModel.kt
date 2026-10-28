package com.example.kloth.ui.screens.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ReviewRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.ui.navigation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // Recupera el ID si viene de la navegación
    private val reviewId: String? = savedStateHandle[AppRoutes.Review.ARG_REVIEW_ID]

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState: StateFlow<ReviewState> = _uiState.asStateFlow()

    init {
        val currentUser = authRepository.currentUser
        _uiState.update {
            it.copy(
                currentUserId = currentUser?.uid,
                currentUserImageUrl = currentUser?.photoUrl?.toString(),
                currentUserName = currentUser?.displayName
                    ?: currentUser?.email?.substringBefore("@")
                    ?: "Usuario"
            )
        }
        currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                userRepository.getUserById(userId).onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            currentUserImageUrl = user.profileImageUrl ?: it.currentUserImageUrl,
                            currentUserName = user.username?.takeIf(String::isNotBlank) ?: it.currentUserName
                        )
                    }
                }
            }
        }
    }

    init {
        // Carga la reseña si el ID existe en la navegación
        reviewId?.let { id ->
            loadReview(id)
        }
    }

    private fun loadReview(id: String) {
        viewModelScope.launch {
            reviewRepository.getReviewById(id, authRepository.currentUser?.uid).onSuccess { resena ->
                _uiState.update { currentState ->
                    currentState.copy(resena = resena)
                }
            }
        }
    }

    fun onTextoComentarioChange(texto: String) {
        _uiState.update { it.copy(nuevoComentarioTexto = texto) }
    }

    fun onEnviarComentario() {
        val currentState = _uiState.value
        val reviewId = reviewId?.takeIf { it.isNotBlank() } ?: return
        val userId = authRepository.currentUser?.uid ?: return
        val text = currentState.nuevoComentarioTexto.trim()
        if (text.isNotBlank() && !currentState.yaComento) {
            viewModelScope.launch {
                reviewRepository.createComment(
                    reviewId = reviewId,
                    userId = userId,
                    username = currentState.currentUserName,
                    profileImageUrl = currentState.currentUserImageUrl,
                    text = text
                ).onSuccess { nuevoComentario ->
                    _uiState.update { latestState ->
                        val comments = if (latestState.ordenMasRecientes) {
                            listOf(nuevoComentario) + latestState.resena.comentariosList
                        } else latestState.resena.comentariosList + nuevoComentario
                        latestState.copy(
                            resena = latestState.resena.copy(comentariosList = comments),
                            nuevoComentarioTexto = ""
                        )
                    }
                }
            }
        }
    }

    fun onLikeResena() {
        val reviewId = reviewId?.takeIf { it.isNotBlank() } ?: return
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            reviewRepository.toggleReviewLike(reviewId, userId).onSuccess { likeState ->
                _uiState.update { currentState ->
                    val review = currentState.resena
                    currentState.copy(
                        resena = review.copy(
                            isLikedByMe = likeState.isLikedByMe,
                            likesCount = likeState.likesCount
                        )
                    )
                }
            }
        }
    }

    fun onDeleteReview() {
        val reviewId = reviewId?.takeIf { it.isNotBlank() } ?: return
        val userId = authRepository.currentUser?.uid ?: return
        if (_uiState.value.resena.authorId != userId || _uiState.value.isDeletingReview) return

        viewModelScope.launch {
            _uiState.update { it.copy(isDeletingReview = true, deleteReviewError = null) }
            reviewRepository.deleteReview(reviewId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeletingReview = false, reviewDeleted = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isDeletingReview = false, deleteReviewError = error.message)
                    }
                }
            )
        }
    }

    fun onLikeComentario(id: String) {
        val reviewId = reviewId?.takeIf { it.isNotBlank() } ?: return
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            reviewRepository.toggleCommentLike(reviewId, id, userId).onSuccess { likeState ->
                _uiState.update { currentState ->
                    val comentariosActualizados = currentState.resena.comentariosList.map { comment ->
                        if (comment.id == id) {
                            comment.copy(
                                isLikedByMe = likeState.isLikedByMe,
                                likesCount = likeState.likesCount
                            )
                        } else comment
                    }
                    currentState.copy(
                        resena = currentState.resena.copy(comentariosList = comentariosActualizados)
                    )
                }
            }
        }
    }

    fun onEliminarComentario(id: String) {
        val reviewId = reviewId?.takeIf { it.isNotBlank() } ?: return
        viewModelScope.launch {
            reviewRepository.deleteComment(reviewId, id).onSuccess {
                _uiState.update { currentState ->
                    val comments = currentState.resena.comentariosList.filterNot { it.id == id }
                    currentState.copy(resena = currentState.resena.copy(comentariosList = comments))
                }
            }
        }
    }

    fun onToggleOrden() {
        _uiState.update { currentState ->
            val nuevoOrden = !currentState.ordenMasRecientes
            val comentariosInvertidos = currentState.resena.comentariosList.reversed()
            currentState.copy(
                ordenMasRecientes = nuevoOrden,
                resena = currentState.resena.copy(comentariosList = comentariosInvertidos)
            )
        }
    }
}
