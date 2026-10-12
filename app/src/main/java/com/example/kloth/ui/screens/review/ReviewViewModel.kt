package com.example.kloth.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.Session
import com.example.kloth.data.repository.CommentRepository
import com.example.kloth.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    // ID de la reseña, llega desde la pantalla vía loadReview
    private var reviewId: String? = null

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState: StateFlow<ReviewState> = _uiState.asStateFlow()

    fun loadReview(reviewId: String) {
        this.reviewId = reviewId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = reviewRepository.getReviewWithComments(reviewId)

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(resena = result.getOrNull() ?: it.resena, isLoading = false)
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun onTextoComentarioChange(texto: String) {
        _uiState.update { it.copy(nuevoComentarioTexto = texto) }
    }

    fun onEnviarComentario() {
        val currentState = _uiState.value
        val reviewId = reviewId ?: return
        if (currentState.nuevoComentarioTexto.isBlank() || currentState.yaComento) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, errorMessage = null) }

            val result = commentRepository.createComment(
                reviewId = reviewId,
                text = currentState.nuevoComentarioTexto.trim(),
                userId = Session.CURRENT_USER_ID
            )

            if (result.isSuccess) {
                val nuevoComentario = result.getOrNull()
                _uiState.update { state ->
                    val lista = if (nuevoComentario == null) {
                        state.resena.comentariosList
                    } else if (state.ordenMasRecientes) {
                        listOf(nuevoComentario) + state.resena.comentariosList
                    } else {
                        state.resena.comentariosList + nuevoComentario
                    }
                    state.copy(
                        resena = state.resena.copy(comentariosList = lista),
                        nuevoComentarioTexto = "",
                        isSending = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isSending = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun onLikeResena() {
        val current = _uiState.value.resena
        val reviewId = reviewId ?: return
        val nuevoLiked = !current.isLikedByMe
        val nuevoCount = if (nuevoLiked) current.likesCount + 1 else current.likesCount - 1

        // Optimista: se pinta al instante y se persiste en el backend
        _uiState.update {
            it.copy(resena = it.resena.copy(isLikedByMe = nuevoLiked, likesCount = nuevoCount))
        }

        viewModelScope.launch {
            val result = reviewRepository.likeReview(reviewId, nuevoCount)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(
                        resena = it.resena.copy(
                            isLikedByMe = current.isLikedByMe,
                            likesCount = current.likesCount
                        ),
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun onLikeComentario(id: String) {
        val comentario = _uiState.value.resena.comentariosList.find { it.id == id } ?: return
        val nuevoLiked = !comentario.isLikedByMe
        val nuevoCount = if (nuevoLiked) comentario.likesCount + 1 else comentario.likesCount - 1

        _uiState.update { currentState ->
            val comentariosActualizados = currentState.resena.comentariosList.map { c ->
                if (c.id == id) c.copy(isLikedByMe = nuevoLiked, likesCount = nuevoCount) else c
            }
            currentState.copy(
                resena = currentState.resena.copy(comentariosList = comentariosActualizados)
            )
        }

        viewModelScope.launch {
            val result = commentRepository.likeComment(id, nuevoCount)
            if (result.isFailure) {
                _uiState.update { currentState ->
                    val revertidos = currentState.resena.comentariosList.map { c ->
                        if (c.id == id) {
                            c.copy(isLikedByMe = comentario.isLikedByMe, likesCount = comentario.likesCount)
                        } else c
                    }
                    currentState.copy(
                        resena = currentState.resena.copy(comentariosList = revertidos),
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun onEliminarComentario(id: String) {
        val previos = _uiState.value.resena.comentariosList

        _uiState.update { currentState ->
            val comentariosActualizados = currentState.resena.comentariosList.filterNot { it.id == id }
            currentState.copy(
                resena = currentState.resena.copy(comentariosList = comentariosActualizados)
            )
        }

        viewModelScope.launch {
            val result = commentRepository.deleteComment(id)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(
                        resena = it.resena.copy(comentariosList = previos),
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun onDeleteReview(onSuccess: () -> Unit = {}) {
        val reviewId = reviewId ?: return
        viewModelScope.launch {
            val result = reviewRepository.deleteReview(reviewId)
            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
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
