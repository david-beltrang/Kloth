package com.example.kloth.ui.screens.modelPics

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.ModelPicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ModelPicViewModel @Inject constructor(
    private val modelPicRepository: ModelPicRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ModelPicState())
    val uiState: StateFlow<ModelPicState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadModelPics() {
        val userId = authRepository.currentUser?.uid
        if (userId.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    modelPics = emptyList(),
                    isLoading = false,
                    errorMessage = "Inicia sesión para ver tus model pics"
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val result = modelPicRepository.getModelPics(userId)
            ensureActive()
            result.onSuccess { modelPics ->
                _uiState.update {
                    it.copy(modelPics = modelPics, isLoading = false, errorMessage = null)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudieron cargar tus model pics"
                    )
                }
            }
        }
    }

    fun selectFilter(type: ModelPicType) {
        _uiState.update { it.copy(selectedFilter = type) }
    }

    /** Sube la foto elegida y guarda sus metadatos en Firestore como una sola acción. */
    fun uploadBaseModelPic(imageUri: Uri, name: String, contentType: String?) {
        val userId = authRepository.currentUser?.uid
        if (userId.isNullOrBlank()) {
            _uiState.update {
                it.copy(uploadErrorMessage = "Inicia sesión para subir una model pic")
            }
            return
        }

        _uiState.update {
            it.copy(
                isUploadingBaseModelPic = true,
                uploadErrorMessage = null,
                createdModelPicId = null
            )
        }

        viewModelScope.launch {
            modelPicRepository.uploadBaseModelPic(userId, name, imageUri, contentType)
                .onSuccess { modelPicId ->
                    _uiState.update {
                        it.copy(isUploadingBaseModelPic = false, createdModelPicId = modelPicId)
                    }
                    loadModelPics()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isUploadingBaseModelPic = false,
                            uploadErrorMessage = error.message ?: "No se pudo subir la model pic"
                        )
                    }
                }
        }
    }

    fun clearUploadResult() {
        _uiState.update { it.copy(createdModelPicId = null, uploadErrorMessage = null) }
    }
}
