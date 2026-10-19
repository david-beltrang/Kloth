package com.example.kloth.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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

    fun registerButtonPressed(){
        val state = _uiState.value
        if (
            state.fullName.isEmpty() ||
            state.email.isEmpty() ||
            state.password.isEmpty() ||
            state.confirmPassword.isEmpty()
        ) {
            _uiState.update { it.copy(showMessage = true, errorMessage = "Todos los campos son obligatorios") }
        } else if (state.password.length < 6) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                )
            }
        } else {
            // Registro en Firebase
            viewModelScope.launch {
                val result = authRepository.signUp(state.email, state.password)
                if (result.isSuccess) {
                    //En cuanto a noSQL y firebase primero se autentica el usuario y se obtiene el ID
                    val userId = authRepository.currentUser?.uid ?: throw Exception("No se pudo obtener el usuario actual")
                    userRepository.registerUser(
                        fullName = state.fullName,
                        userId = userId
                    )

                    _uiState.update { it.copy(navigate = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            showMessage = true,
                            errorMessage = result.exceptionOrNull()?.message ?: "Ocurrió un error al crear tu cuenta"
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
