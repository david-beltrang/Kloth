package com.example.kloth.ui.screens.tryOn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.ModelPicRepository
import com.example.kloth.data.repository.TryOnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TryOnViewModel @Inject constructor(
    private val modelPicRepository: ModelPicRepository,
    private val tryOnRepository: TryOnRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TryOnState())
    val uiState: StateFlow<TryOnState> = _uiState.asStateFlow()
    private var modelPicsLoadJob: Job? = null
    private var generationJob: Job? = null

    fun openDialog(publicationId: String, garmentImageUrl: String?) {
        modelPicsLoadJob?.cancel()
        generationJob?.cancel()
        _uiState.value = TryOnState(
            isDialogVisible = true,
            publicationId = publicationId,
            garmentImageUrl = garmentImageUrl,
            isLoadingModelPics = true
        )

        val userId = authRepository.currentUser?.uid
        if (userId.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    isLoadingModelPics = false,
                    phase = TryOnPhase.ERROR,
                    errorMessage = "Inicia sesión para usar Try On"
                )
            }
            return
        }

        modelPicsLoadJob = viewModelScope.launch {
            modelPicRepository.getModelPics(userId)
                .onSuccess { modelPics ->
                    _uiState.update {
                        it.copy(
                            baseModelPics = modelPics.filter { pic -> pic.type == ModelPicType.BASE },
                            isLoadingModelPics = false,
                            phase = TryOnPhase.SELECTING,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoadingModelPics = false,
                            phase = TryOnPhase.ERROR,
                            errorMessage = error.message ?: "No se pudieron cargar tus fotos"
                        )
                    }
                }
        }
    }

    fun selectBaseModelPic(modelPicId: String) {
        _uiState.update {
            it.copy(
                selectedBaseModelPicId = if (it.selectedBaseModelPicId == modelPicId) null else modelPicId,
                phase = TryOnPhase.SELECTING,
                errorMessage = null
            )
        }
    }

    fun retryLoadModelPics() {
        val currentState = _uiState.value
        val publicationId = currentState.publicationId ?: return
        openDialog(publicationId, currentState.garmentImageUrl)
    }

    fun requestGeneration() {
        val state = _uiState.value
        if (state.phase == TryOnPhase.GENERATING) return
        val publicationId = state.publicationId ?: return
        val baseModelPicId = state.selectedBaseModelPicId ?: return
        if (authRepository.currentUser == null) {
            showGenerationError("Inicia sesión para usar Try On")
            return
        }

        _uiState.update {
            it.copy(phase = TryOnPhase.GENERATING, errorMessage = null)
        }

        generationJob?.cancel()
        generationJob = viewModelScope.launch {
            tryOnRepository.generateTryOn(publicationId, baseModelPicId)
                .onSuccess { result ->
                    showGeneratedResult(result.imageUrl, result.modelPicId)
                }
                .onFailure { error ->
                    showGenerationError(error.message ?: "No se pudo generar la imagen")
                }
        }
    }

    private fun showGeneratedResult(imageUrl: String, modelPicId: String) {
        _uiState.update {
            it.copy(
                phase = TryOnPhase.RESULT,
                resultImageUrl = imageUrl,
                resultModelPicId = modelPicId,
                errorMessage = null
            )
        }
    }

    private fun showGenerationError(message: String) {
        _uiState.update { it.copy(phase = TryOnPhase.ERROR, errorMessage = message) }
    }

    fun dismissDialog() {
        modelPicsLoadJob?.cancel()
        generationJob?.cancel()
        _uiState.value = TryOnState()
    }

    fun onContinueConsumed() {
        dismissDialog()
    }
}
