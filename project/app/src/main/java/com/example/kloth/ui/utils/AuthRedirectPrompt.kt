package com.example.kloth.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.theme.KlothTheme

/**
 * Componente genérico para mostrar un mensaje informativo seguido de un enlace clicable.
 * Se utiliza principalmente para alternar entre pantallas de autenticación (Login/Registro).
 *
 * @param text Texto informativo (ej: "¿No tienes cuenta?").
 * @param linkText Texto del enlace accionable (ej: "Regístrate").
 * @param onLinkClick Acción a ejecutar al pulsar el enlace.
 */
@Composable
fun AuthRedirectPrompt(
    text: String,
    linkText: String,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = linkText,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable(onClick = onLinkClick)
                .padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthRedirectPromptPreview() {
    KlothTheme {
        AuthRedirectPrompt(
            text = "¿No tienes cuenta?",
            linkText = "Regístrate",
            onLinkClick = {}
        )
    }
}
