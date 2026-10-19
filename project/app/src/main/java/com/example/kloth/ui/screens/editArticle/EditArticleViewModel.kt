package com.example.kloth.ui.screens.editArticle

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.dto.UpdateArticleDto
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.ui.navigation.AppRoutes
import com.example.kloth.ui.screens.createArticle.components.ArticleType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository,
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val articleId: String =
        savedStateHandle.get<String>(AppRoutes.EditArticle.ARG_ARTICLE_ID).orEmpty()

    private val _uiState = MutableStateFlow(EditArticleState())
    val uiState: StateFlow<EditArticleState> = _uiState.asStateFlow()

    init {
        loadArticle()
    }

    fun loadArticle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            articleRepository.getArticleForEdit(articleId)
                .onSuccess { a ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            type = ArticleType.entries.firstOrNull { t -> t.name.equals(a.categoryTag, ignoreCase = true) } ?: ArticleType.PRENDA,
                            originalPhotoUrl = a.imageUrl,
                            photo = a.imageUrl?.let { url -> Uri.parse(url) },
                            nombre = a.title,
                            descripcion = a.description,
                            marca = a.brand,
                            color = a.colorName,
                            precio = a.price.toDoubleOrNull()
                                ?.let { p -> if (p % 1.0 == 0.0) p.toLong().toString() else p.toString() }
                                .orEmpty(),
                            estilo = a.style.orEmpty(),
                            ciudad = a.city.orEmpty(),
                            pais = a.country.orEmpty(),
                            organizador = a.organizer.orEmpty(),
                            sitioWeb = a.website.orEmpty(),
                            paisOrigen = a.originCountry.orEmpty(),
                            anioFundacion = a.foundedAt?.toString().orEmpty()
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message.orEmpty()) }
                }
        }
    }

    fun onNombreChanged(v: String) = _uiState.update { it.copy(nombre = v) }
    fun onDescripcionChanged(v: String) = _uiState.update { it.copy(descripcion = v) }
    fun onMarcaChanged(v: String) = _uiState.update { it.copy(marca = v) }
    fun onColorChanged(v: String) = _uiState.update { it.copy(color = v) }
    fun onPrecioChanged(v: String) = _uiState.update { it.copy(precio = v) }
    fun onEstiloChanged(v: String) = _uiState.update { it.copy(estilo = v) }
    fun onCiudadChanged(v: String) = _uiState.update { it.copy(ciudad = v) }
    fun onPaisChanged(v: String) = _uiState.update { it.copy(pais = v) }
    fun onOrganizadorChanged(v: String) = _uiState.update { it.copy(organizador = v) }
    fun onSitioWebChanged(v: String) = _uiState.update { it.copy(sitioWeb = v) }
    fun onPaisOrigenChanged(v: String) = _uiState.update { it.copy(paisOrigen = v) }

    fun onAnioFundacionChanged(v: String) {
        if (v.length <= 4 && v.all { it.isDigit() }) {
            _uiState.update { it.copy(anioFundacion = v) }
        }
    }

    fun onPhotoPicked(uri: Uri) = _uiState.update { it.copy(photo = uri) }

    /** La X: quita la foto para poder escoger otra. */
    fun onRemovePhoto() = _uiState.update { it.copy(photo = null) }

    fun saveChanges(onSuccess: () -> Unit) {
        val s = _uiState.value
        if (s.nombre.isBlank() || s.isSaving) return


        val isPrenda = s.type == ArticleType.PRENDA
        val isOutfit = s.type == ArticleType.OUTFIT
        val isEvento = s.type == ArticleType.EVENTO
        val isMarca = s.type == ArticleType.MARCA

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            // Foto: sin foto -> vacío; la original (una URL) -> se conserva; una de la galería -> se sube
            val photo = s.photo
            val imageUrl = when {
                photo == null -> ""
                photo.scheme == "http" || photo.scheme == "https" -> photo.toString()
                else -> storageRepository.uploadArticleImage(photo).getOrElse { error ->
                    Log.e("EditArticleViewModel", "No se pudo subir la imagen", error)
                    _uiState.update { it.copy(isSaving = false) }
                    return@launch
                }
            }

            val dto = UpdateArticleDto(
                title = s.nombre.trim(),
                description = s.descripcion.trim(),
                imageArticle = imageUrl,
                updatedAt = Instant.now().toString(),
                brand = s.marca.takeIf { isPrenda && it.isNotBlank() },
                color = s.color.takeIf { isPrenda && it.isNotBlank() },
                price = if (isPrenda) s.precio.toDoubleOrNull() else null,
                style = s.estilo.takeIf { isOutfit && it.isNotBlank() },
                city = s.ciudad.takeIf { isEvento && it.isNotBlank() },
                country = s.pais.takeIf { isEvento && it.isNotBlank() },
                organizer = s.organizador.takeIf { isEvento && it.isNotBlank() },
                website = s.sitioWeb.trim().takeIf { isMarca && it.isNotBlank() },
                originCountry = s.paisOrigen.trim().takeIf { isMarca && it.isNotBlank() },
                foundedAt = if (isMarca) s.anioFundacion.toIntOrNull() else null
            )

            val result = articleRepository.updateArticle(articleId, dto, s.originalPhotoUrl)
            _uiState.update { it.copy(isSaving = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                Log.e("EditArticleViewModel", "Error al guardar los cambios", result.exceptionOrNull())
            }
        }
    }
}