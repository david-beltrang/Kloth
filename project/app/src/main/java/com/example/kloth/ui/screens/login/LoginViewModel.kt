package com.example.kloth.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.R
import com.example.kloth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, showMessage = false, errorMessage = "") }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, showMessage = false, errorMessage = "") }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        if (_uiState.value.email.isEmpty() || _uiState.value.password.isEmpty()) {
            _uiState.update { 
                it.copy(
                    showMessage = true, 
                    errorMessage = context.getString(R.string.login_error_empty_fields)
                ) 
            }
        } else {
            _uiState.update { it.copy(isLoading = true, showMessage = false, errorMessage = "") }
            viewModelScope.launch{
                val result = authRepository.signIn(
                    _uiState.value.email,
                    _uiState.value.password
                )

                if(result.isSuccess){
                    _uiState.update { it.copy(navigate = true, isLoading = false)}
                } else {
                    val mensaje = result.exceptionOrNull()?.message ?: context.getString(R.string.login_error_generic)
                    _uiState.update {it.copy(showMessage = true, errorMessage = mensaje, isLoading = false)}
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
