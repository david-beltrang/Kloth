package com.example.kloth.ui.screens.createArticlle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.screens.createArticlle.components.*
import com.example.kloth.ui.screens.explore.components.CategoryChip
import com.example.kloth.ui.utils.KlothBottomNavigation
import com.example.kloth.R
import java.util.Locale

/**
 * Pantalla principal que aplica STATE HOISTING. 
 * Toda la lógica de "memoria" (estados) vive aquí, y los componentes de abajo solo dibujan.
 */
@Composable
fun CreateArticleScreenContent(
    modifier: Modifier = Modifier
) {
    // state hosting
    // Guardamos toda la información aquí para que los componentes hijos sean Stateless
    var selectedType by remember { mutableStateOf(ArticleType.PRENDA) }
    val photos = remember { mutableStateListOf(R.drawable.abrigo_negro, R.drawable.bolso_cuero) }
    
    // Variables para el formulario
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var estilo by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var organizador by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // El TopBar recibe la acción por parámetro
            TopAppBarCreateArticle(onCloseClick = { /* Acción de cerrar */ })
        },
        bottomBar = {
            KlothBottomNavigation(selectedItem = 2)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Selector de Categorías (Chips con OnClick por parámetro)
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ArticleType.entries.forEach { type ->
                    CategoryChip(
                        text = type.name.lowercase().replaceFirstChar { it.titlecase(Locale.ROOT) },
                        isSelected = selectedType == type,
                        onClick = { selectedType = type }
                    )
                }
            }

            // 2. Card de imagen (OnClick por parámetro)
            CardCreateArticle(
                onClick = { /* Lógica para añadir foto */ }

            )

            // 3. Carrusel (Callback por parámetro para borrar)
            if (photos.isNotEmpty()) {
                PhotoCarrusel(
                    photos = photos,
                    onDeletePhoto = { index -> photos.removeAt(index) }
                )
            }

            // 4. Formulario (State Hoisting Puro: recibe datos y callbacks)
            FormArticle(
                selectedType = selectedType,
                nombre = nombre,
                onNombreChange = { nombre = it },
                descripcion = descripcion,
                onDescripcionChange = { descripcion = it },
                marca = marca,
                onMarcaChange = { marca = it },
                color = color,
                onColorChange = { color = it },
                precio = precio,
                onPrecioChange = { precio = it },
                estilo = estilo,
                onEstiloChange = { estilo = it },
                ciudad = ciudad,
                onCiudadChange = { ciudad = it },
                pais = pais,
                onPaisChange = { pais = it },
                organizador = organizador,
                onOrganizadorChange = { organizador = it },
                onPublicarClick = { /* Lógica de guardado final */ }
            )
        }
    }
}
