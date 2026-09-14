package com.example.kloth.ui.screens.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.theme.RedInferno

/*
Este composable se usa en las publicaciones como etiqueta del tipo de artículo.
Es de tipo box para poder sobreponer el texto en el ovalo.

Es el composable llamado TabRow de Material3
link de referencia: https://www.jetpackcompose.pro/tabs/tabrow/
 */
@Composable
fun KlothBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondary, 
    contentColor: Color = MaterialTheme.colorScheme.onSecondary
) {
    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(9999.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KlothBadgePreview() {
    KlothTheme {
        KlothBadge(
            text = "COUTURE",
            containerColor = RedInferno
        )
    }
}
