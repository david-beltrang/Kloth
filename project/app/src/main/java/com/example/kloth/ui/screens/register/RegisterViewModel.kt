package com.example.kloth.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.R
import com.example.kloth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    //Declarar variable privada para el e3stado y no repetir mutableStateFlow
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    //Logica de negocio de los campos y reaccionar a eventoss
    fun onFullNameChange(input: String) {
        _uiState.update {it.copy(fullName = input)}
    }
    fun onEmailChange(input: String) {
        _uiState.update {it.copy(email = input)}
    }
    fun onPasswordChange(input: String) {
        _uiState.update {it.copy(password = input)}
    }
    fun onConfirmPasswordChange(input: String) {
        _uiState.update {it.copy(confirmPassword = input)}
    }

    //Evitar navegacion
    fun registerButtonPressed(){
        if(
            _uiState.value.fullName.isEmpty() ||
            _uiState.value.email.isEmpty() ||
            _uiState.value.password.isEmpty() ||
            _uiState.value.confirmPassword.isEmpty()
        ){
            _uiState.update { it.copy(mostrarMensaje = true, errorMessageRes = R.string.register_error_empty_fields) }
        } else {
            viewModelScope.launch {
                try{
                    authRepository.signUp(_uiState.value.email, _uiState.value.password)
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