package com.example.kloth.ui.screens.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        if (_uiState.value.email.isEmpty() || _uiState.value.password.isEmpty()) {
            _uiState.update { 
                it.copy(
                    showMessage = true, 
                    credentialsMessage = "Por favor ingresa todos los campos"
                ) 
            }
        } else {
            viewModelScope.launch{
                val result = authRepository.signIn(
                    _uiState.value.email,
                    _uiState.value.password
                )

                if(result.isSuccess){
                    _uiState.update { it.copy(navigate = true)}
                } else {
                    val mensaje = result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
                    _uiState.update {it.copy(showMessage = true, errorMessage = mensaje)}
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
