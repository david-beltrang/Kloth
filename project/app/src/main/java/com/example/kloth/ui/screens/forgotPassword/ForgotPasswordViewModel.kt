package com.example.kloth.ui.screens.forgotPassword

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.kloth.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onSendClick() {
        if (_uiState.value.email.isEmpty()) {
            _uiState.update {
                it.copy(
                    showMessage = true,
                    errorMessage = context.getString(R.string.forgot_password_error_empty_email)
                )
            }
        } else {
            // Aquí iría la llamada al servidor, por ahora simulamos éxito
            _uiState.update { it.copy(navigateBack = true) }
        }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigateBack = false) }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(showMessage = false) }
    }
}
