package com.example.kloth.ui.screens.notification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificacionAvatar(
    inicial: String,
    mostrarPuntoNoLeido: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(48.dp)) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = inicial.uppercase(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp
            )
        }
        if (mostrarPuntoNoLeido) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
            )
        }
    }
}
