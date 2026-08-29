package com.example.kloth.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.login.components.LoginHeader
import com.example.kloth.ui.screens.login.components.LoginOrDivider
import com.example.kloth.ui.utils.AuthRedirectPrompt
import com.example.kloth.ui.utils.KlothPrimaryButton
import com.example.kloth.ui.utils.KlothTextField

@Composable
fun RegisterScreenContent(
    // Variables de estado (Datos)
    fullName: String,
    email: String,
    password: String,
    confirmPassword: String,
    isPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,

    // Métodos para manejar el estado (Eventos)
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordToggleClick: () -> Unit,
    onConfirmPasswordToggleClick: () -> Unit,

    // Navegación
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,

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
                LoginHeader()

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.register_title),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                KlothTextField(
                    value = fullName,
                    onValueChange = onFullNameChange,
                    placeholder = stringResource(R.string.register_name_placeholder),
                    leadingIcon = Icons.Outlined.Person,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                KlothTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = stringResource(R.string.login_email_placeholder),
                    leadingIcon = Icons.Outlined.Email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                KlothTextField(
                    // Estado y Eventos elevados
                    value = password,
                    onValueChange = onPasswordChange,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordToggleClick = onPasswordToggleClick,

                    // Estética y Configuración
                    placeholder = stringResource(R.string.login_password_placeholder),
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                KlothTextField(
                    // Estado y Eventos elevados
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    isPasswordVisible = isConfirmPasswordVisible,
                    onPasswordToggleClick = onConfirmPasswordToggleClick,

                    // Estética y Configuración
                    placeholder = stringResource(R.string.register_confirm_password_placeholder),
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onRegisterClick()
                        }
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                KlothPrimaryButton(
                    text = stringResource(R.string.register_button),
                    onClick = onRegisterClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                LoginOrDivider()

                Spacer(modifier = Modifier.height(24.dp))

                AuthRedirectPrompt(
                    text = stringResource(R.string.register_has_account),
                    linkText = stringResource(R.string.register_login),
                    onLinkClick = onLoginClick
                )
            }
    }
}
