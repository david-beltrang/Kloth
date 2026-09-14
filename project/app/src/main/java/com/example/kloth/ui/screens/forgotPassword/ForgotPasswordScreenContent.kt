package com.example.kloth.ui.screens.forgotPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.forgotPassword.components.ForgotPasswordHeader
import com.example.kloth.ui.utils.KlothPrimaryButton
import com.example.kloth.ui.utils.KlothTextField

@Composable
fun ForgotPasswordScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ForgotPasswordHeader()

            Spacer(modifier = Modifier.height(40.dp))

            KlothTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = stringResource(R.string.login_email_placeholder),
                leadingIcon = Icons.Outlined.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSendClick()
                    }
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            KlothPrimaryButton(
                text = stringResource(R.string.forgot_password_button),
                onClick = onSendClick,
                enabled = email.isNotBlank()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.forgot_password_back_login),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable(onClick = onBackClick)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
    }
}
