package com.example.kloth.ui.screens.reviewsMap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.ReviewRepository
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
class ReviewsMapViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewsMapState())
    val uiState: StateFlow<ReviewsMapState> = _uiState.asStateFlow()

    // Si se pide de nuevo (refrescar o reintentar) se cancela la anterior
    private var loadJob: Job? = null

    fun loadRecentReviews() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val result = reviewRepository.getRecentReviewPins()
            ensureActive() // si esta carga se cancelo, no se toca el estado
            result.fold(
                onSuccess = { pins ->
                    _uiState.update {
                        it.copy(pins = pins, isLoading = false, errorMessage = null)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
            )
        }
    }

    fun onPinSelected(reviewId: String) {
        val pin = _uiState.value.pins.firstOrNull { it.reviewId == reviewId } ?: return
        _uiState.update { it.copy(selectedPin = pin) }
    }

    fun onPinDismissed() {
        _uiState.update { it.copy(selectedPin = null) }
    }
}
