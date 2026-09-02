package com.example.kloth.ui.screens.login

import androidx.lifecycle.ViewModel
import com.example.kloth.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

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
                    errorMessageRes = R.string.login_error_empty_fields 
                ) 
            }
        } else {
            // Aquí iría la validación con el servidor, por ahora simulamos éxito
            _uiState.update { it.copy(navigate = true) }
        }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigate = false) }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(showMessage = false) }
    }
}
