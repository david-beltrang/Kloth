package com.example.kloth.ui.screens.forgotPassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    ForgotPasswordScreenContent(
        modifier = modifier,
        email = email,
        onEmailChange = { email = it },
        onSendClick = {},
        onBackClick = onBackClick
    )
}
