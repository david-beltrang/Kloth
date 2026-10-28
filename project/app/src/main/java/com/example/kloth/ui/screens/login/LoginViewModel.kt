package com.example.kloth.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.R
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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
                    errorMessage = context.getString(R.string.login_error_empty_fields)
                ) 
            }
        } else if (password.length < 6) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    errorMessage = context.getString(R.string.auth_error_password_short)
                )
            }
        } else {
            viewModelScope.launch {
                val result = authRepository.signIn(email, password)
                if (result.isSuccess) {
                    runCatching {
                        val userId = authRepository.currentUser?.uid
                        val token = FirebaseMessaging.getInstance().token.await()
                        if (userId != null) userRepository.updateFcmToken(userId, token)
                    }
                    _uiState.update { it.copy(navigate = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.exceptionOrNull()?.message
                                ?: context.getString(R.string.login_error_generic),
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
