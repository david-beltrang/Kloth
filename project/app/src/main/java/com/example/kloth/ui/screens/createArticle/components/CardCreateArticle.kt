package com.example.kloth.ui.screens.createArticle.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

/**
 * Este componente es "estático". No tiene memoria propia (no usa remember).
 * Si le pasas una foto por [imageResId], la muestra. Si no, muestra el cuadro punteado.
 */
@Composable
fun CardCreateArticle(
    modifier: Modifier = Modifier,
    imageResId: Int? = null, // Parámetro opcional para la imagen
    onClick: () -> Unit = {}
) {
    val outlineColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            // Solo dibujamos el borde punteado si no hay una imagen puesta
            .then(
                if (imageResId == null) {
                    Modifier.drawBehind {
                        val stroke = Stroke(
                            width = 1.4.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(15f, 4f),
                                phase = 0f
                            )
                        )
                        drawRoundRect(
                            color = outlineColor,
                            style = stroke,
                            cornerRadius = CornerRadius(12.dp.toPx())
                        )
                    }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imageResId != null) {
            // Si hay id de imagen entonces s epinta
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Si no hay imagen, mostramos el diseño de "Añadir"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddAPhoto,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Añadir Foto",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Toca para subir imágenes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// --- PREVIEWS PARA VER AMBOS ESTADOS ---

@Preview(showBackground = true, name = "Estado Vacío")
@Composable
fun CardEmptyPreview() {
    KlothTheme {
        CardCreateArticle(imageResId = null)
    }
}

@Preview(showBackground = true, name = "Con Imagen Seleccionada")
@Composable
fun CardWithPhotoPreview() {
    KlothTheme {
        // Aquí simulamos que ya se agregó una foto
        CardCreateArticle(imageResId = R.drawable.abrigo_negro)
    }
}
