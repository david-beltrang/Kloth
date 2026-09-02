package com.example.kloth.ui.screens.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.kloth.R
import com.example.kloth.data.ComentarioReviewData
import com.example.kloth.data.ResenasDataLocal
import com.example.kloth.ui.navigation.AppRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReviewViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Recupera el ID si viene de la navegación
    private val reviewId: String? = savedStateHandle[AppRoutes.Review.ARG_REVIEW_ID]

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState: StateFlow<ReviewState> = _uiState.asStateFlow()

    init {
        // Carga la reseña si el ID existe en la navegación
        reviewId?.let { id ->
            loadReview(id)
        }
    }

    private fun loadReview(id: String) {
        val resenaEncontrada = ResenasDataLocal.obtenerResenaPorId(id)
        resenaEncontrada?.let { resena ->
            _uiState.update { currentState ->
                currentState.copy(resena = resena)
            }
        }
    }

    fun onTextoComentarioChange(texto: String) {
        _uiState.update { it.copy(nuevoComentarioTexto = texto) }
    }

    fun onEnviarComentario() {
        val currentState = _uiState.value
        if (currentState.nuevoComentarioTexto.isNotBlank() && !currentState.yaComento) {
            val nuevoComentario = ComentarioReviewData(
                id = "my_c_${System.currentTimeMillis()}",
                authorName = "Tú",
                timeAgo = "Ahora",
                avatarRes = R.drawable.abrigo_negro,
                commentText = currentState.nuevoComentarioTexto,
                likesCount = 0,
                isMyComment = true
            )

            val listaActualizada = if (currentState.ordenMasRecientes) {
                listOf(nuevoComentario) + currentState.resena.comentariosList
            } else {
                currentState.resena.comentariosList + nuevoComentario
            }

            _uiState.update {
                it.copy(
                    resena = it.resena.copy(comentariosList = listaActualizada),
                    nuevoComentarioTexto = ""
                )
            }
        }
    }

    fun onLikeResena() {
        _uiState.update { currentState ->
            val resenaActual = currentState.resena
            val nuevoLiked = !resenaActual.isLikedByMe
            val nuevoCount = if (nuevoLiked) resenaActual.likesCount + 1 else resenaActual.likesCount - 1
            currentState.copy(
                resena = resenaActual.copy(
                    isLikedByMe = nuevoLiked,
                    likesCount = nuevoCount
                )
            )
        }
    }

    fun onLikeComentario(id: String) {
        _uiState.update { currentState ->
            val comentariosActualizados = currentState.resena.comentariosList.map { c ->
                if (c.id == id) {
                    val nuevoLiked = !c.isLikedByMe
                    c.copy(
                        isLikedByMe = nuevoLiked,
                        likesCount = if (nuevoLiked) c.likesCount + 1 else c.likesCount - 1
                    )
                } else c
            }
            currentState.copy(
                resena = currentState.resena.copy(comentariosList = comentariosActualizados)
            )
        }
    }

    fun onEliminarComentario(id: String) {
        _uiState.update { currentState ->
            val comentariosActualizados = currentState.resena.comentariosList.filterNot { it.id == id }
            currentState.copy(
                resena = currentState.resena.copy(comentariosList = comentariosActualizados)
            )
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