package com.example.kloth.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.R
import com.example.kloth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun onFullNameChange(input: String) {
        _uiState.update { it.copy(fullName = input, showMessage = false, errorMessage = "") }
    }

    fun onEmailChange(input: String) {
        _uiState.update { it.copy(email = input, showMessage = false, errorMessage = "") }
    }

    fun onPasswordChange(input: String) {
        _uiState.update { it.copy(password = input, showMessage = false, errorMessage = "") }
    }

    fun onConfirmPasswordChange(input: String) {
        _uiState.update { it.copy(confirmPassword = input, showMessage = false, errorMessage = "") }
    }

    fun registerButtonPressed() {
        if (
            _uiState.value.fullName.isEmpty() ||
            _uiState.value.email.isEmpty() ||
            _uiState.value.password.isEmpty() ||
            _uiState.value.confirmPassword.isEmpty()
        ) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    errorMessage = "Todos los campos son obligatorios"
                )
            }
        } else {
            _uiState.update { it.copy(showMessage = false, errorMessage = "") }
            viewModelScope.launch {
                val result = authRepository.signUp(
                    _uiState.value.email,
                    _uiState.value.password
                )

                if (result.isSuccess) {
                    _uiState.update { it.copy(navigate = true) }
                } else {
                    val mensaje = result.exceptionOrNull()?.message ?: "Error al registrarse"
                    _uiState.update {
                        it.copy(
                            showMessage = true,
                            errorMessage = mensaje
                        )
                    }
                }
            }
        }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(showMessage = false) }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigate = false) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }
}
