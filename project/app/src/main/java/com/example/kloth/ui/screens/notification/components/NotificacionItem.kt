package com.example.kloth.ui.screens.notification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun NotificacionItem(
    notificacion: NotificacionUI,
    onToggleSeguir: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NotificacionAvatar(
            inicial = notificacion.nombreUsuario.take(1),
            mostrarPuntoNoLeido = notificacion.noLeida
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(notificacion.nombreUsuario)
                    }
                    append(" ")
                    append(notificacion.descripcion)
                },
                fontSize = 14.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = notificacion.tiempo,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        when (notificacion.tipo) {
            TipoNotificacion.NUEVO_SEGUIDOR -> {
                BotonSeguir(
                    estaSiguiendo = notificacion.estaSiguiendo,
                    onClick = { onToggleSeguir(notificacion.id) },
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            else -> {
                if (notificacion.tieneMiniatura) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificacionItemPreview() {
    KlothTheme {
        NotificacionItem(
            notificacion = NotificacionUI(
                id = "1",
                nombreUsuario = "alex_style",
                descripcion = "comentó tu reseña",
                tiempo = "Hace 18 min",
                tipo = TipoNotificacion.COMENTARIO,
                tieneMiniatura = true,
                noLeida = true
            ),
            onToggleSeguir = {}
        )
    }
}
