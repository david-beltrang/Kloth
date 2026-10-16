package com.example.kloth.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, showMessage = false) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, showMessage = false) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update { 
                it.copy(
                    showMessage = true, 
                    errorMessage = "Por favor, completa todos los campos"
                ) 
            }
        } else if (password.length < 6) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                )
            }
        } else {
            viewModelScope.launch {
                val result = authRepository.signIn(email, password)
                if (result.isSuccess) {
                    _uiState.update { it.copy(navigate = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.exceptionOrNull()?.message ?: "Credenciales incorrectas o error de conexión",
                            showMessage = true
                        )
                    }
                }
            }
        }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigate = false) }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(showMessage = false) }
    }
}
