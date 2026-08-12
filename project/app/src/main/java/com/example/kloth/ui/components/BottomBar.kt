package com.example.kloth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

@Composable
// Barra de navegación inferior con las 5 secciones de la app
fun BottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(painter = painterResource(R.drawable.home), label = stringResource(R.string.nav_home), isSelected = false)
        BottomNavItem(painter = painterResource(R.drawable.search), label = stringResource(R.string.nav_explore), isSelected = true)
        BottomNavItem(painter = painterResource(R.drawable.create), label = stringResource(R.string.nav_create), isSelected = false)
        BottomNavItem(painter = painterResource(R.drawable.notification), label = stringResource(R.string.nav_alerts), isSelected = false)
        BottomNavItem(painter = painterResource(R.drawable.profile), label = stringResource(R.string.nav_profile), isSelected = false)
    }
}

@Composable
// Ítem individual de la barra inferior (icono + etiqueta), resaltado si está seleccionado
private fun BottomNavItem(
    painter: Painter,
    label: String,
    isSelected: Boolean
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // Cada uno de los iconos de la Barra
        Icon(
            painter = painter,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    KlothTheme {
        BottomBar()
    }
}
