package com.example.kloth.ui.screens.forgotPassword

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onSendClick() {
        if (_uiState.value.email.isEmpty()) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    errorMessage = "Por favor, ingresa tu correo electrónico"
                )
            }
        } else {
            // Aquí iría la llamada al servidor, por ahora simulamos éxito
            _uiState.update { it.copy(navigateBack = true) }
        }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigateBack = false) }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(showMessage = false) }
    }
}
