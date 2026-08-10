package com.example.kloth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R

@Composable
// Barra de navegación inferior con las 5 secciones de la app
fun BottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colorResource(id = R.color.surface))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(icon = Icons.Filled.Home, label = stringResource(R.string.nav_home), isSelected = false)
        BottomNavItem(icon = Icons.Filled.Explore, label = stringResource(R.string.nav_explore), isSelected = true)
        BottomNavItem(icon = Icons.Filled.AddCircle, label = stringResource(R.string.nav_create), isSelected = false)
        BottomNavItem(icon = Icons.Filled.Notifications, label = stringResource(R.string.nav_alerts), isSelected = false)
        BottomNavItem(icon = Icons.Filled.Person, label = stringResource(R.string.nav_profile), isSelected = false)
    }
}

@Composable
// Ítem individual de la barra inferior (icono + etiqueta), resaltado si está seleccionado
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean
) {
    val backgroundColor = if (isSelected) colorResource(id = R.color.secondary_container) else colorResource(id = R.color.surface)
    val contentColor = if (isSelected) colorResource(id = R.color.on_secondary_container) else colorResource(id = R.color.on_surface_variant)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // Cada uno de los iconos de la Barra
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}