package com.example.kloth.ui.screens.createArticlle.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun CardCreateArticle(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val outlineColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp) // 1. Tamaño fijo para el contenedor
            .padding(12.dp) // Margen exterior
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick) // todo el container interactivo
            .drawBehind {
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
            .padding(vertical = 32.dp, horizontal = 24.dp), // 3. Espaciado interno visual
        contentAlignment = Alignment.Center
    ) {
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

@Preview(showBackground = true)
@Composable
fun CardCreateArticlePreview() {
    KlothTheme {
        CardCreateArticle(onClick = {})
    }
}
