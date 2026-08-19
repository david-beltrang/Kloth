package com.example.kloth.ui.screens.notification
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.notificaciones.componentes.NotificacionItem
import com.example.kloth.notificaciones.componentes.NotificacionUI
import com.example.kloth.notificaciones.componentes.NotificacionesDataLocal
import com.example.kloth.notificaciones.componentes.NotificacionesTopBar

/**
 * Pantalla de Notificaciones. Une el TopBar + la lista de NotificacionItem.
 *
 * IMPORTANTE — un solo Scaffold por app:
 * Esta pantalla NO trae su propio Scaffold ni su propia barra de navegación
 * inferior. Debe usarse como "content" dentro del ÚNICO Scaffold de la app
 * (el que ya tienen armado con la bottom nav bar general). Ejemplo de cómo
 * conectarla en tu NavHost:
 *
 * Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
 *     NavHost(navController, startDestination = "home", Modifier.padding(padding)) {
 *         ...
 *         composable("notificaciones") {
 *             NotificacionesScreen(
 *                 onBackClick = { navController.popBackStack() },
 *                 onSearchClick = { navController.navigate("busqueda") }
 *             )
 *         }
 *     }
 * }
 */
@Composable
fun NotificacionesScreen(
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    notificacionesIniciales: List<NotificacionUI> = NotificacionesDataLocal.notificacionesEjemplo,
    modifier: Modifier = Modifier
) {
    // State hoisting: el estado de la lista completa vive AQUÍ, a nivel de
    // pantalla. Los componentes hijos (NotificacionItem -> BotonSeguir) son
    // "sin estado": reciben valores y suben eventos mediante lambdas.
    var notificaciones by remember { mutableStateOf(notificacionesIniciales) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NotificacionesTopBar(
            onBackClick = onBackClick,
            onSearchClick = onSearchClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(notificaciones, key = { it.id }) { notificacion ->
                NotificacionItem(
                    notificacion = notificacion,
                    onToggleSeguir = { id ->
                        notificaciones = notificaciones.map {
                            if (it.id == id) it.copy(estaSiguiendo = !it.estaSiguiendo) else it
                        }
                    }
                )
            }
        }
    }
}
