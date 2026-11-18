package com.example.kloth.ui.screens.forgotPassword.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.forgotPassword.ForgotPasswordStep
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun ForgotPasswordHeader(
    step: ForgotPasswordStep = ForgotPasswordStep.Email,
    modifier: Modifier = Modifier
) {
    val logoResource = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
        R.drawable.logo_blanco
    } else {
        R.drawable.logo_negro
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = logoResource),
            contentDescription = stringResource(R.string.cd_logo),
            modifier = Modifier.size(height = 90.dp, width = 420.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(if (step == ForgotPasswordStep.Email) R.string.forgot_password_title else R.string.forgot_password_code_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordHeaderPreview() {
    KlothTheme {
        ForgotPasswordHeader()
    }
}
