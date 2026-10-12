package com.example.kloth.ui.screens.createArticle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CreateArticleScreen(
    onPublicarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CreateArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            onPublicarClick()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CreateArticleScreenContent(
            uiState = uiState,

            onTypeChange = { viewModel.onTypeSelected(it) },
            onAddPhotoClick = { viewModel.onAddPhotoMock() },
            onDeletePhoto = { viewModel.onDeletePhoto(it) },
            onNombreChange = { viewModel.onNombreChanged(it) },
            onDescripcionChange = { viewModel.onDescripcionChanged(it) },
            onMarcaChange = { viewModel.onMarcaChanged(it) },
            onColorChange = { viewModel.onColorChanged(it) },
            onPrecioChange = { viewModel.onPrecioChanged(it) },
            onEstiloChange = { viewModel.onEstiloChanged(it) },
            onCiudadChange = { viewModel.onCiudadChanged(it) },
            onPaisChange = { viewModel.onPaisChanged(it) },
            onOrganizadorChange = { viewModel.onOrganizadorChanged(it) },
            onTagInputChange = { viewModel.onTagInputChange(it) },
            onAddTag = { viewModel.onAddTag() },
            onRemoveTag = { viewModel.onRemoveTag(it) },

            onPublicarClick = {
                viewModel.publishArticle()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
