package com.example.kloth.ui.screens.notification.components

import android.content.res.Configuration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

/**
 * Botón de acción para seguir o dejar de seguir a un usuario.
 * Alterna entre estados 'Seguir' y 'Siguiendo' con estilos visuales distintos.
 */
@Composable
fun FollowButton(
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
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                text = stringResource(R.string.label_following),
                fontSize = 12.sp
            )
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
            Text(
                text = stringResource(R.string.label_follow),
                fontSize = 12.sp
            )
        }
    }
}

@Preview(name = "Seguir Claro", showBackground = true)
@Preview(name = "Seguir Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FollowButtonPreview() {
    KlothTheme {
        FollowButton(estaSiguiendo = false, onClick = {})
    }
}

@Preview(name = "Siguiendo Claro", showBackground = true)
@Preview(name = "Siguiendo Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FollowingButtonPreview() {
    KlothTheme {
        FollowButton(estaSiguiendo = true, onClick = {})
    }
}
