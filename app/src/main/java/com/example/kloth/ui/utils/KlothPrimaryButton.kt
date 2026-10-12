package com.example.kloth.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.theme.KlothTheme

/*
Botón principal de acción (login, registro, confirmar).
Es el Button de Material3 con forma píldora, alineado con el CTA de perfil.

link de referencia: https://m3.material.io/components/buttons/overview
 */
@Composable
fun KlothPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(horizontal = 24.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun KlothPrimaryButtonPreview() {
    KlothTheme(darkTheme = false) {
        KlothPrimaryButton(
            text = "Iniciar sesión",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KlothPrimaryButtonDarkPreview() {
    KlothTheme(darkTheme = true) {
        KlothPrimaryButton(
            text = "Iniciar sesión",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
