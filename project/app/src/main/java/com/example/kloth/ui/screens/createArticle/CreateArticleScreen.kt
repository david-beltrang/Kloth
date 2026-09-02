package com.example.kloth.ui.screens.createArticle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateArticleScreen(
    onPublicarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CreateArticleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CreateArticleScreenContent(
            uiState = uiState,
            onTypeChange = viewModel::onTypeSelected,
            onAddPhotoClick = viewModel::onAddPhotoMock,
            onDeletePhoto = viewModel::onDeletePhoto,
            onNombreChange = viewModel::onNombreChanged,
            onDescripcionChange = viewModel::onDescripcionChanged,
            onMarcaChange = viewModel::onMarcaChanged,
            onColorChange = viewModel::onColorChanged,
            onPrecioChange = viewModel::onPrecioChanged,
            onEstiloChange = viewModel::onEstiloChanged,
            onCiudadChange = viewModel::onCiudadChanged,
            onPaisChange = viewModel::onPaisChanged,
            onOrganizadorChange = viewModel::onOrganizadorChanged,
            onPublicarClick = {
                viewModel.publishArticle(onSuccess = onPublicarClick)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}