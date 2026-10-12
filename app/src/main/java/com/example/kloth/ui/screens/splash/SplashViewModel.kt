package com.example.kloth.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState

    init {
        checkUser()
    }

    private fun checkUser() {
        viewModelScope.launch {
            delay(SPLASH_DELAY_MS)
            if (authRepository.currentUser != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        navigateToFeed = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        navigateToLogin = true
                    )
                }
            }
        }
    }

    fun onNavigationConsumed() {
        _uiState.update {
            it.copy(
                navigateToFeed = false,
                navigateToLogin = false
            )
        }
    }

    companion object {
        private const val SPLASH_DELAY_MS = 1500L
    }
}
