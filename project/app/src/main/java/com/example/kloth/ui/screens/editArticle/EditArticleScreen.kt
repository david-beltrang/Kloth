package com.example.kloth.ui.screens.editArticle

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kloth.R

@Composable
fun EditArticleScreen(
    onSaved: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: EditArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Abre la galería del teléfono; devuelve null si el usuario cancela
    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onPhotoPicked(it) }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(R.string.edit_article_error, uiState.errorMessage.orEmpty()))
                    Button(onClick = { viewModel.loadArticle() }) {
                        Text(text = stringResource(R.string.edit_article_retry))
                    }
                }
            }

            else -> {
                EditArticleScreenContent(
                    uiState = uiState,
                    onAddPhotoClick = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onRemovePhoto = { viewModel.onRemovePhoto() },
                    onNombreChange = { viewModel.onNombreChanged(it) },
                    onDescripcionChange = { viewModel.onDescripcionChanged(it) },
                    onMarcaChange = { viewModel.onMarcaChanged(it) },
                    onColorChange = { viewModel.onColorChanged(it) },
                    onPrecioChange = { viewModel.onPrecioChanged(it) },
                    onEstiloChange = { viewModel.onEstiloChanged(it) },
                    onCiudadChange = { viewModel.onCiudadChanged(it) },
                    onPaisChange = { viewModel.onPaisChanged(it) },
                    onOrganizadorChange = { viewModel.onOrganizadorChanged(it) },
                    onSitioWebChange = { viewModel.onSitioWebChanged(it) },
                    onPaisOrigenChange = { viewModel.onPaisOrigenChanged(it) },
                    onAnioFundacionChange = { viewModel.onAnioFundacionChanged(it) },
                    onSaveClick = { viewModel.saveChanges(onSuccess = onSaved) },
                    onCloseClick = onCloseClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}