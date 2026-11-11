package com.example.kloth.ui.screens.forgotPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.forgotPassword.components.CodeAndPasswordStep
import com.example.kloth.ui.screens.forgotPassword.components.ForgotPasswordEmailStep
import com.example.kloth.ui.screens.forgotPassword.components.ForgotPasswordHeader

@Composable
fun ForgotPasswordScreenContent(
    state: ForgotPasswordState,
    onEmailChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNewPasswordToggle: () -> Unit,
    onConfirmPasswordToggle: () -> Unit,
    onPrimaryClick: () -> Unit,
    onResendClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ForgotPasswordHeader(step = state.step)
        Spacer(Modifier.height(28.dp))

        if (state.step == ForgotPasswordStep.Email) {
            ForgotPasswordEmailStep(
                email = state.email,
                onEmailChange = onEmailChange,
                onSubmit = onPrimaryClick,
                enabled = !state.isLoading
            )
        } else {
            CodeAndPasswordStep(
                state = state,
                onCodeChange = onCodeChange,
                onNewPasswordChange = onNewPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onNewPasswordToggle = onNewPasswordToggle,
                onConfirmPasswordToggle = onConfirmPasswordToggle,
                onSubmit = onPrimaryClick,
                onResend = onResendClick,
                enabled = !state.isLoading
            )
        }

        if (state.showMessage && state.errorMessage.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = state.errorMessage,
                color = if (state.messageIsSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (state.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        Spacer(Modifier.height(28.dp))
        Text(
            text = stringResource(R.string.forgot_password_back_login),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable(onClick = onBackClick).padding(6.dp)
        )
    }
}
