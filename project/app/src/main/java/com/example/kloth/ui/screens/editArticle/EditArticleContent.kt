package com.example.kloth.ui.screens.editArticle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.screens.createArticle.components.ArticleType
import com.example.kloth.ui.screens.createArticle.components.FormArticle
import com.example.kloth.ui.screens.editArticle.components.PhotoEdit
import com.example.kloth.ui.screens.explore.components.CategoryChip

@Composable
fun EditArticleScreenContent(
    uiState: EditArticleState,
    onAddPhotoClick: () -> Unit,
    onRemovePhoto: () -> Unit,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onMarcaChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onEstiloChange: (String) -> Unit,
    onCiudadChange: (String) -> Unit,
    onPaisChange: (String) -> Unit,
    onOrganizadorChange: (String) -> Unit,
    onSitioWebChange: (String) -> Unit,
    onPaisOrigenChange: (String) -> Unit,
    onAnioFundacionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCloseClick:() -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Tipo del artículo (solo lectura: no se puede cambiar al editar)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close)
                )
            }
            val typeName = when (uiState.type) {
                ArticleType.PRENDA -> stringResource(R.string.type_prenda)
                ArticleType.OUTFIT -> stringResource(R.string.type_outfit)
                ArticleType.EVENTO -> stringResource(R.string.type_evento)
                ArticleType.MARCA -> stringResource(R.string.type_marca)
            }
            CategoryChip(text = typeName, isSelected = true, onClick = {})
        }

        // 2. Foto principal con la X para cambiarla
        PhotoEdit(
            photo = uiState.photo,
            onPickClick = onAddPhotoClick,
            onRemoveClick = onRemovePhoto
        )

        // 3. Formulario (el mismo de crear)
        FormArticle(
            selectedType = uiState.type,
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
            sitioWeb = uiState.sitioWeb,
            onSitioWebChange = onSitioWebChange,
            paisOrigen = uiState.paisOrigen,
            onPaisOrigenChange = onPaisOrigenChange,
            anioFundacion = uiState.anioFundacion,
            onAnioFundacionChange = onAnioFundacionChange,
            onPublicarClick = onSaveClick,
            buttonTextRes = R.string.btn_guardar_cambios,
            buttonEnabled = !uiState.isSaving
        )
    }
}