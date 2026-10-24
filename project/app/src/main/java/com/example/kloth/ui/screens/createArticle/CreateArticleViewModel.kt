package com.example.kloth.ui.screens.createArticle

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.R
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.ui.screens.createArticle.components.ArticleType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

@HiltViewModel
class CreateArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
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

    fun onFechaInicioChanged(fecha: String) {
        _uiState.update { it.copy(fechaInicio = fecha) }
    }

    fun onFechaFinChanged(fecha: String) {
        _uiState.update { it.copy(fechaFin = fecha) }
    }

    fun onOrganizadorChanged(organizador: String) {
        _uiState.update { it.copy(organizador = organizador) }
    }
    fun onSitioWebChanged(sitioWeb: String) {
        _uiState.update { it.copy(sitioWeb = sitioWeb) }
    }

    fun onPaisOrigenChanged(paisOrigen: String) {
        _uiState.update { it.copy(paisOrigen = paisOrigen) }
    }

    fun onAnioFundacionChanged(anio: String) {
        if (anio.length <= 4 && anio.all { it.isDigit() }) {
            _uiState.update { it.copy(anioFundacion = anio) }
        }
    }
    fun onPhotoPicked(uri: Uri) {
        _uiState.update { it.copy(photos = it.photos + uri) }
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
        val state = _uiState.value
        val uid = authRepository.currentUser?.uid ?: return
        if (state.nombre.isBlank() || state.isPublishing) return

        val isPrenda = state.selectedType == ArticleType.PRENDA
        val isOutfit = state.selectedType == ArticleType.OUTFIT
        val isEvento = state.selectedType == ArticleType.EVENTO
        val isMarca = state.selectedType == ArticleType.MARCA

        viewModelScope.launch {
            _uiState.update { it.copy(isPublishing = true) }

            // 1. Subir la foto (si el usuario escogió una) y obtener su URL
            var imageUrl = ""
            val firstPhoto = state.photos.firstOrNull()
            if (firstPhoto != null) {
                val upload = storageRepository.uploadArticleImage(firstPhoto)
                if (upload.isFailure) {
                    Log.e("CreateArticleViewModel", "No se pudo subir la imagen", upload.exceptionOrNull())
                    _uiState.update { it.copy(isPublishing = false) }
                    return@launch
                }
                imageUrl = upload.getOrThrow()
            }

            // 2. Guardar el artículo con la URL de la imagen
            val dto = CreateArticleDto(
                typeArticle = state.selectedType.name,
                title = state.nombre.trim(),
                description = state.descripcion.trim(),
                idUser = uid,
                imageArticle = imageUrl,
                createdAt = Instant.now().toString(),
                brand = state.marca.takeIf { isPrenda && it.isNotBlank() },
                color = state.color.takeIf { isPrenda && it.isNotBlank() },
                price = if (isPrenda) state.precio.toDoubleOrNull() else null,
                style = state.estilo.takeIf { isOutfit && it.isNotBlank() },
                city = state.ciudad.takeIf { isEvento && it.isNotBlank() },
                country = state.pais.takeIf { isEvento && it.isNotBlank() },
                startDate = state.fechaInicio.takeIf { isEvento && it.isNotBlank() },
                endDate = state.fechaFin.takeIf { isEvento && it.isNotBlank() },
                organizer = state.organizador.takeIf { isEvento && it.isNotBlank() },
                website = state.sitioWeb.trim().takeIf { isMarca && it.isNotBlank() },
                originCountry = state.paisOrigen.trim().takeIf { isMarca && it.isNotBlank() },
                foundedAt = if (isMarca) state.anioFundacion.toIntOrNull() else null
            )

            val result = articleRepository.createArticle(dto)
            _uiState.update { it.copy(isPublishing = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                Log.e("CreateArticleViewModel", "Error al publicar el artículo", result.exceptionOrNull())
            }
        }
    }
}

