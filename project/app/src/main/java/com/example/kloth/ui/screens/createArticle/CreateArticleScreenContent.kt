package com.example.kloth.ui.screens.createArticle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.screens.createArticle.components.*
import com.example.kloth.ui.screens.explore.components.CategoryChip
import java.util.Locale

/**
 * Contenido sin estado de la pantalla. Los estados viven hoisted en CreateArticleScreen,
 * y este composable solo dibuja a partir de los parámetros y callbacks recibidos.
 */
@Composable
fun CreateArticleScreenContent(
    selectedType: ArticleType,
    onTypeChange: (ArticleType) -> Unit,
    photos: List<Int>,
    onDeletePhoto: (Int) -> Unit,
    nombre: String,
    onNombreChange: (String) -> Unit,
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    marca: String,
    onMarcaChange: (String) -> Unit,
    color: String,
    onColorChange: (String) -> Unit,
    precio: String,
    onPrecioChange: (String) -> Unit,
    estilo: String,
    onEstiloChange: (String) -> Unit,
    ciudad: String,
    onCiudadChange: (String) -> Unit,
    pais: String,
    onPaisChange: (String) -> Unit,
    organizador: String,
    onOrganizadorChange: (String) -> Unit,
    onPublicarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
                    onClick = { onTypeChange(type) }
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
                onDeletePhoto = onDeletePhoto
            )
        }

        // 4. Formulario (State Hoisting Puro: recibe datos y callbacks)
        FormArticle(
            selectedType = selectedType,
            nombre = nombre,
            onNombreChange = onNombreChange,
            descripcion = descripcion,
            onDescripcionChange = onDescripcionChange,
            marca = marca,
            onMarcaChange = onMarcaChange,
            color = color,
            onColorChange = onColorChange,
            precio = precio,
            onPrecioChange = onPrecioChange,
            estilo = estilo,
            onEstiloChange = onEstiloChange,
            ciudad = ciudad,
            onCiudadChange = onCiudadChange,
            pais = pais,
            onPaisChange = onPaisChange,
            organizador = organizador,
            onOrganizadorChange = onOrganizadorChange,
            onPublicarClick = onPublicarClick
        )
    }
}
