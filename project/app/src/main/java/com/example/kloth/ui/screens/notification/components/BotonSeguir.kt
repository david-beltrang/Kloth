package com.example.kloth.ui.screens.notification.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BotonSeguir(
    estaSiguiendo: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)

    if (estaSiguiendo) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(32.dp),
            contentPadding = contentPadding,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Siguiendo", fontSize = 12.sp)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier.height(32.dp),
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Seguir", fontSize = 12.sp)
        }
    }
}
