package com.example.kloth.ui.screens.createArticle

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateArticleScreen(
    onPublicarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CreateArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onPhotoPicked(it) }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CreateArticleScreenContent(
            //El state para variables de estado
            uiState = uiState,

            //metodos de estado
            onTypeChange = { viewModel.onTypeSelected(it) },
            onAddPhotoClick = {
                photoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onDeletePhoto = { viewModel.onDeletePhoto(it) },
            onNombreChange = { viewModel.onNombreChanged(it) },
            onDescripcionChange = { viewModel.onDescripcionChanged(it) },
            onMarcaChange = { viewModel.onMarcaChanged(it) },
            onColorChange = { viewModel.onColorChanged(it) },
            onPrecioChange = { viewModel.onPrecioChanged(it) },
            onEstiloChange = { viewModel.onEstiloChanged(it) },
            onCiudadChange = { viewModel.onCiudadChanged(it) },
            onPaisChange = { viewModel.onPaisChanged(it) },
            onFechaInicioChange = { viewModel.onFechaInicioChanged(it) },
            onFechaFinChange = { viewModel.onFechaFinChanged(it) },
            onOrganizadorChange = { viewModel.onOrganizadorChanged(it) },
            onSitioWebChange = { viewModel.onSitioWebChanged(it) },
            onPaisOrigenChange = { viewModel.onPaisOrigenChanged(it) },
            onAnioFundacionChange = { viewModel.onAnioFundacionChanged(it) },

            //Navegacion y logica de publicar lista para implmentar
            onPublicarClick = {
                viewModel.publishArticle(onSuccess = onPublicarClick)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
