package com.example.kloth.ui.components.utils

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.theme.KlothTheme

/*
Este composable se usa en todas las pantallas y funciona como barra de navegacion.
Es de tipo NavigationBar, se añado el item de home, buscador, crear, notficaciones, perfil.

Es el composable llamado NavigationBar
link de referencia: https://m3.material.io/components/navigation-bar/specs
 */
@Composable
fun KlothBottomNavigation(
    modifier: Modifier = Modifier, 
    selectedItem: Int = 0
) {
    NavigationBar(
        modifier = modifier.height(64.dp),
        // Usamos 'surface' para que sea clarito (blanco/crema) en modo claro
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { /* Navegación desactivada */ },
            icon = { 
                Icon(
                    // SI está seleccionado (==0) usa Filled, SINO usa Outlined
                    imageVector = if (selectedItem == 0) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                ) 
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                // Los iconos ahora son Negros (primary) cuando se seleccionan
                selectedIconColor = MaterialTheme.colorScheme.primary,
                // Y grises (onSurfaceVariant) cuando no lo están
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { /* Navegación desactivada */ },
            icon = { 
                Icon(
                    // SI está seleccionado (==0) usa Filled, SINO usa Outlined
                    imageVector = if (selectedItem == 1) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Search"
                ) 
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { /* Navegación desactivada */ },
            icon = { 
                Icon(
                    // SI está seleccionado (==0) usa Filled, SINO usa Outlined
                    imageVector = if (selectedItem == 2) Icons.Filled.AddBox else Icons.Outlined.AddBox,
                    contentDescription = "AddBox"
                ) 
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
        )
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = { /* Navegación desactivada */ },
            icon = {
                NotificationCount(
                    count = 5,
                    icon = if (selectedItem == 3) Icons.Filled.Notifications else Icons.Outlined.Notifications
                )
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
        )
        NavigationBarItem(
            selected = selectedItem == 4,
            onClick = { /* Navegación desactivada */ },
            icon = { 
                Icon(
                    // SI está seleccionado (==0) usa Filled, SINO usa Outlined
                    imageVector = if (selectedItem == 4) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Home"
                ) 
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KlothBottomNavigationPreview() {
    KlothTheme(darkTheme = false) { // Forzamos el modo claro en el preview
        KlothBottomNavigation()
    }
}
