package com.example.kloth.ui.screens.forgotPassword.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.utils.KlothPrimaryButton
import com.example.kloth.ui.utils.KlothTextField

@Composable
fun ForgotPasswordEmailStep(
    email: String,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    enabled: Boolean
) {
    val focusManager = LocalFocusManager.current
    Text(
        text = stringResource(R.string.forgot_password_instruction),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(Modifier.height(20.dp))
    KlothTextField(
        value = email,
        onValueChange = onEmailChange,
        placeholder = stringResource(R.string.login_email_placeholder),
        leadingIcon = Icons.Outlined.Email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); onSubmit() })
    )
    Spacer(Modifier.height(24.dp))
    KlothPrimaryButton(
        text = stringResource(R.string.forgot_password_button),
        onClick = onSubmit,
        enabled = enabled && email.isNotBlank()
    )
}
