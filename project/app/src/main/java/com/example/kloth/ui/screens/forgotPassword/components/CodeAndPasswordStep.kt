package com.example.kloth.ui.screens.forgotPassword.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordState
import com.example.kloth.ui.utils.KlothPrimaryButton
import com.example.kloth.ui.utils.KlothTextField

@Composable
fun CodeAndPasswordStep(
    state: ForgotPasswordState,
    onCodeChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNewPasswordToggle: () -> Unit,
    onConfirmPasswordToggle: () -> Unit,
    onSubmit: () -> Unit,
    onResend: () -> Unit,
    enabled: Boolean
) {
    Text(
        text = stringResource(R.string.forgot_password_code_instruction),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(Modifier.height(16.dp))
    KlothTextField(
        value = state.code,
        onValueChange = onCodeChange,
        placeholder = stringResource(R.string.forgot_password_code_placeholder),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    )
    Spacer(Modifier.height(12.dp))
    KlothTextField(
        value = state.newPassword,
        onValueChange = onNewPasswordChange,
        placeholder = stringResource(R.string.forgot_password_new_password_placeholder),
        isPassword = true,
        isPasswordVisible = state.isNewPasswordVisible,
        onPasswordToggleClick = onNewPasswordToggle,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
    )
    Spacer(Modifier.height(12.dp))
    KlothTextField(
        value = state.confirmPassword,
        onValueChange = onConfirmPasswordChange,
        placeholder = stringResource(R.string.forgot_password_confirm_password_placeholder),
        isPassword = true,
        isPasswordVisible = state.isConfirmPasswordVisible,
        onPasswordToggleClick = onConfirmPasswordToggle,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
    )
    Spacer(Modifier.height(20.dp))
    KlothPrimaryButton(
        text = stringResource(R.string.forgot_password_reset_button),
        onClick = onSubmit,
        enabled = enabled
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = if (state.resendSeconds > 0) {
            stringResource(R.string.forgot_password_resend_countdown, state.resendSeconds)
        } else {
            stringResource(R.string.forgot_password_resend)
        },
        color = MaterialTheme.colorScheme.primary.copy(alpha = if (state.resendSeconds == 0) 1f else 0.55f),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.clickable(enabled = state.resendSeconds == 0 && enabled, onClick = onResend)
    )
}
