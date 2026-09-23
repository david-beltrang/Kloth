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
                    errorMessageRes = R.string.login_error_empty_fields 
                ) 
            }
        } else if (password.length < 6) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    errorMessageRes = R.string.auth_error_password_short
                )
            }
        } else {
            viewModelScope.launch{
                try{
                    authRepository.signIn(email, password)
                    _uiState.update { it.copy(navigate = true) }
                }catch(e: Exception){
                    _uiState.update { it.copy(errorMessageRes = R.string.login_error_generic, showMessage = true) }
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
