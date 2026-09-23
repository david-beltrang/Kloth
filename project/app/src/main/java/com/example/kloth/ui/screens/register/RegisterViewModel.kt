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

    //Declarar variable privada para el estado y no repetir mutableStateFlow
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    //Logica de negocio de los campos y reaccionar a eventoss
    fun onFullNameChange(input: String) {
        _uiState.update {it.copy(fullName = input, mostrarMensaje = false)}
    }
    fun onEmailChange(input: String) {
        _uiState.update {it.copy(email = input, mostrarMensaje = false)}
    }
    fun onPasswordChange(input: String) {
        _uiState.update {it.copy(password = input, mostrarMensaje = false)}
    }
    fun onConfirmPasswordChange(input: String) {
        _uiState.update {it.copy(confirmPassword = input, mostrarMensaje = false)}
    }

    //Evitar navegacion
    fun registerButtonPressed(){
        val state = _uiState.value
        if(
            state.fullName.isEmpty() ||
            state.email.isEmpty() ||
            state.password.isEmpty() ||
            state.confirmPassword.isEmpty()
        ){
            _uiState.update { it.copy(mostrarMensaje = true, errorMessageRes = R.string.register_error_empty_fields) }
        } else if (state.password.length < 6) {
            _uiState.update {
                it.copy(
                    mostrarMensaje = true,
                    errorMessageRes = R.string.auth_error_password_short
                )
            }
        } else {
            viewModelScope.launch {
                try{
                    authRepository.signUp(state.email, state.password)
                    _uiState.update { it.copy(navigate = true) }
                }catch (e: Exception){
                    _uiState.update {
                        it.copy(
                            mostrarMensaje = true,
                            errorMessageRes = R.string.register_error_generic
                        )
                    }
                }
            }
        }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(mostrarMensaje = false) }
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
