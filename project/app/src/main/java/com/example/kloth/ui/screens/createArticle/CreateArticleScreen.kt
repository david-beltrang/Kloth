package com.example.kloth.ui.screens.createArticle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.kloth.R
import com.example.kloth.ui.screens.createArticle.components.ArticleType

@Composable
fun CreateArticleScreen(
    onPublicarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // state hoisting
    var selectedType by remember { mutableStateOf(ArticleType.PRENDA) }
    val photos = remember { mutableStateListOf(R.drawable.abrigo_negro, R.drawable.bolso_cuero) }
    
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var estilo by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var organizador by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CreateArticleScreenContent(
            selectedType = selectedType,
            onTypeChange = { selectedType = it },
            photos = photos,
            onDeletePhoto = { index -> photos.removeAt(index) },
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
            onPublicarClick = onPublicarClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}
