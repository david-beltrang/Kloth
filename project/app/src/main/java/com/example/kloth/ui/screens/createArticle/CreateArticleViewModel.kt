package com.example.kloth.ui.screens.createArticle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.R
import com.example.kloth.data.Session
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.ui.screens.createArticle.components.ArticleType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreateArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateArticleState())
    val uiState: StateFlow<CreateArticleState> = _uiState.asStateFlow()

    fun onTypeSelected(type: ArticleType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun onNombreChanged(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
    }

    fun onDescripcionChanged(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion) }
    }

    fun onMarcaChanged(marca: String) {
        _uiState.update { it.copy(marca = marca) }
    }

    fun onColorChanged(color: String) {
        _uiState.update { it.copy(color = color) }
    }

    fun onPrecioChanged(precio: String) {
        _uiState.update { it.copy(precio = precio) }
    }

    fun onEstiloChanged(estilo: String) {
        _uiState.update { it.copy(estilo = estilo) }
    }

    fun onCiudadChanged(ciudad: String) {
        _uiState.update { it.copy(ciudad = ciudad) }
    }

    fun onPaisChanged(pais: String) {
        _uiState.update { it.copy(pais = pais) }
    }

    fun onOrganizadorChanged(organizador: String) {
        _uiState.update { it.copy(organizador = organizador) }
    }

    fun onTagInputChange(value: String) {
        _uiState.update { it.copy(tagInput = value) }
    }

    fun onAddTag() {
        _uiState.update { state ->
            val tag = state.tagInput.trim().removePrefix("#").replace(" ", "-").lowercase()
            if (tag.isEmpty() || state.tags.contains(tag)) {
                state.copy(tagInput = "")
            } else {
                state.copy(tags = state.tags + tag, tagInput = "")
            }
        }
    }

    fun onRemoveTag(tag: String) {
        _uiState.update { it.copy(tags = it.tags - tag) }
    }

    fun onAddPhotoMock() {
        _uiState.update { currentState ->
            val updatedPhotos = currentState.photos + R.drawable.camiseta_gymshark
            currentState.copy(photos = updatedPhotos)
        }
    }

    fun onDeletePhoto(index: Int) {
        _uiState.update { currentState ->
            if (index in currentState.photos.indices) {
                val updatedPhotos = currentState.photos.toMutableList().apply { removeAt(index) }
                currentState.copy(photos = updatedPhotos)
            } else {
                currentState
            }
        }
    }

    fun publishArticle() {
        val state = _uiState.value
        if (state.isPublishing) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPublishing = true, errorMessage = null) }

            val result = articleRepository.createArticle(
                CreateArticleDto(
                    typeArticle = state.selectedType.name,
                    title = state.nombre.trim(),
                    description = state.descripcion.trim(),
                    idUser = Session.CURRENT_USER_ID,
                    imageArticle = "",
                    eliminated = false,
                    tags = state.tags
                )
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isPublishing = false, navigateBack = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isPublishing = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}
