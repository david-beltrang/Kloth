package com.example.kloth.ui.screens.createArticle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.createArticle.components.ArticleType
import com.example.kloth.ui.screens.createArticle.components.CardCreateArticle
import com.example.kloth.ui.screens.createArticle.components.FormArticle
import com.example.kloth.ui.screens.createArticle.components.PhotoCarrusel
import com.example.kloth.ui.screens.explore.components.CategoryChip

/**
 * Contenido sin estado de la pantalla. Dibuja a partir del objeto UiState recibido.
 */
@Composable
fun CreateArticleScreenContent(
    uiState: CreateArticleState,
    onTypeChange: (ArticleType) -> Unit,
    onAddPhotoClick: () -> Unit,
    onDeletePhoto: (Int) -> Unit,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onMarcaChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onEstiloChange: (String) -> Unit,
    onCiudadChange: (String) -> Unit,
    onPaisChange: (String) -> Unit,
    onOrganizadorChange: (String) -> Unit,
    onPublicarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Selector de Categorías
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ArticleType.entries.forEach { type ->
                val typeName = when (type) {
                    ArticleType.PRENDA -> stringResource(R.string.type_prenda)
                    ArticleType.OUTFIT -> stringResource(R.string.type_outfit)
                    ArticleType.EVENTO -> stringResource(R.string.type_evento)
                }
                CategoryChip(
                    text = typeName,
                    isSelected = uiState.selectedType == type,
                    onClick = { onTypeChange(type) }
                )
            }
        }

        // 2. Card de imagen
        CardCreateArticle(
            onClick = onAddPhotoClick
        )

        // 3. Carrusel
        if (uiState.photos.isNotEmpty()) {
            PhotoCarrusel(
                photos = uiState.photos,
                onDeletePhoto = onDeletePhoto
            )
        }

        // 4. Formulario
        FormArticle(
            selectedType = uiState.selectedType,
            nombre = uiState.nombre,
            onNombreChange = onNombreChange,
            descripcion = uiState.descripcion,
            onDescripcionChange = onDescripcionChange,
            marca = uiState.marca,
            onMarcaChange = onMarcaChange,
            color = uiState.color,
            onColorChange = onColorChange,
            precio = uiState.precio,
            onPrecioChange = onPrecioChange,
            estilo = uiState.estilo,
            onEstiloChange = onEstiloChange,
            ciudad = uiState.ciudad,
            onCiudadChange = onCiudadChange,
            pais = uiState.pais,
            onPaisChange = onPaisChange,
            organizador = uiState.organizador,
            onOrganizadorChange = onOrganizadorChange,
            onPublicarClick = onPublicarClick
        )
    }
}