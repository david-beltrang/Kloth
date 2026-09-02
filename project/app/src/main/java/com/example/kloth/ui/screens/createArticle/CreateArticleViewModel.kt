package com.example.kloth.ui.screens.createArticle

import androidx.lifecycle.ViewModel
import com.example.kloth.R
import com.example.kloth.ui.screens.createArticle.components.ArticleType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateArticleViewModel : ViewModel() {

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

    fun publishArticle(onSuccess: () -> Unit) {
        // Lógica simulada de publicación
        onSuccess()
    }
}