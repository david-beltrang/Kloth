package com.example.kloth.ui.screens.notification.components

import android.content.res.Configuration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.data.local.NotificacionUI
import com.example.kloth.data.local.TipoNotificacion
import com.example.kloth.ui.theme.KlothTheme

// Fila individual que muestra el autor, la accion, el tiempo y el boton Seguir o la miniatura.
@Composable
fun NotificationItem(
    notificacion: NotificacionUI,
    onToggleSeguir: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val description = stringResource(
        when (notificacion.tipo) {
            TipoNotificacion.NUEVO_SEGUIDOR -> com.example.kloth.R.string.notification_followed_you
            TipoNotificacion.NUEVA_RESENA -> com.example.kloth.R.string.notification_new_review
            TipoNotificacion.COMENTARIO -> com.example.kloth.R.string.notification_commented_review
            TipoNotificacion.LIKE -> com.example.kloth.R.string.notification_liked_review
        }
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NotificationAvatar(
            inicial = notificacion.nombreUsuario.take(1),
            mostrarPuntoNoLeido = notificacion.noLeida,
            imageUrl = notificacion.avatarUrl
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)) {
                        append(notificacion.nombreUsuario)
                    }
                    append(" ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                        append(description)
                    }
                },
                fontSize = 14.sp,
                lineHeight = 18.sp
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
                FollowButton(
                    estaSiguiendo = notificacion.estaSiguiendo,
                    onClick = { onToggleSeguir(notificacion.id) },
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            else -> {
                if (notificacion.tieneMiniatura) {
                    if (notificacion.thumbnailUrl.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        )
                    } else {
                        coil.compose.AsyncImage(
                            model = notificacion.thumbnailUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotificationItemPreview() {
    KlothTheme {
        NotificationItem(
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
