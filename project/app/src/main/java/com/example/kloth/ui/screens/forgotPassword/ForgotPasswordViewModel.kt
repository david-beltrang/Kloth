package com.example.kloth.ui.screens.forgotPassword

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.R
import com.example.kloth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState
    private var countdownJob: Job? = null

    fun onEmailChange(email: String) = _uiState.update {
        it.copy(email = email, showMessage = false, errorMessage = "")
    }

    fun onCodeChange(code: String) = _uiState.update {
        it.copy(code = code.filter(Char::isDigit).take(6), showMessage = false, errorMessage = "")
    }

    fun onNewPasswordChange(password: String) = _uiState.update {
        it.copy(newPassword = password, showMessage = false, errorMessage = "")
    }

    fun onConfirmPasswordChange(password: String) = _uiState.update {
        it.copy(confirmPassword = password, showMessage = false, errorMessage = "")
    }

    fun toggleNewPasswordVisibility() = _uiState.update {
        it.copy(isNewPasswordVisible = !it.isNewPasswordVisible)
    }

    fun toggleConfirmPasswordVisibility() = _uiState.update {
        it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
    }

    fun onSendClick() = requestCode()

    fun onResendClick() {
        if (_uiState.value.resendSeconds == 0 && !_uiState.value.isLoading) requestCode()
    }

    private fun requestCode() {
        val email = _uiState.value.email.trim()
        if (!EMAIL_PATTERN.matches(email)) {
            showError(R.string.forgot_password_error_invalid_email)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showMessage = false, errorMessage = "") }
            val result = authRepository.requestPasswordResetCode(email)
            _uiState.update { it.copy(isLoading = false) }
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            step = ForgotPasswordStep.CodeAndPassword,
                            resendSeconds = 60,
                            errorMessage = context.getString(R.string.forgot_password_request_success),
                            showMessage = true,
                            messageIsSuccess = true
                        )
                    }
                    startCountdown()
                },
                onFailure = { showError(it.message ?: context.getString(R.string.auth_error_generic)) }
            )
        }
    }

    fun onResetClick() {
        val state = _uiState.value
        if (!Regex("^\\d{6}$").matches(state.code)) {
            showError(R.string.forgot_password_error_invalid_code)
            return
        }
        if (state.newPassword.length < 6) {
            showError(R.string.auth_error_password_short)
            return
        }
        if (state.newPassword != state.confirmPassword) {
            showError(R.string.forgot_password_error_password_mismatch)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showMessage = false, errorMessage = "") }
            val result = authRepository.resetPasswordWithCode(state.email.trim(), state.code, state.newPassword)
            result.fold(
                onSuccess = {
                    countdownJob?.cancel()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = context.getString(R.string.forgot_password_reset_success),
                            showMessage = true,
                            messageIsSuccess = true
                        )
                    }
                    delay(SUCCESS_MESSAGE_DURATION_MS)
                    _uiState.update { it.copy(navigateBack = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false) }
                    showError(it.message ?: context.getString(R.string.auth_error_generic))
                }
            )
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (_uiState.value.resendSeconds > 0) {
                delay(1_000)
                _uiState.update { it.copy(resendSeconds = (it.resendSeconds - 1).coerceAtLeast(0)) }
            }
        }
    }

    private fun showError(resourceId: Int) = showError(context.getString(resourceId))

    private fun showError(message: String) = _uiState.update {
        it.copy(errorMessage = message, showMessage = true, messageIsSuccess = false)
    }

    fun onNavigationConsumed() = _uiState.update { it.copy(navigateBack = false) }

    fun onMessageShown() = _uiState.update { it.copy(showMessage = false) }

    private companion object {
        val EMAIL_PATTERN = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
        const val SUCCESS_MESSAGE_DURATION_MS = 2_000L
    }
}
