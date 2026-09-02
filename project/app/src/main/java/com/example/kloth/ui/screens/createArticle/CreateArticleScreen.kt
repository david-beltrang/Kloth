package com.example.kloth.ui.screens.createArticle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateArticleScreen(
    onPublicarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CreateArticleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CreateArticleScreenContent(
            //El state para variables de estado
            uiState = uiState,

            //metodos de estado
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
            onPublicarClick = {
                viewModel.publishArticle(onSuccess = onPublicarClick)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
