package com.example.kloth.ui.screens.feed

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.kloth.R
import com.example.kloth.ui.screens.feed.components.FeedTabRow

@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel,
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {}
) {
    // Escuchar el estado del ViewModel
    val state by feedViewModel.uiState.collectAsState()

    // Android 17 (API 37) exige el permiso de red local para llegar al backend en 10.0.2.2.
    // Si el usuario lo acepta, se vuelven a pedir los articulos.
    val context = LocalContext.current
    val localNetworkLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) feedViewModel.getAllPosts() }

    LaunchedEffect(Unit) {
        val needsPermission = Build.VERSION.SDK_INT >= 37 &&
            ContextCompat.checkSelfPermission(context, LOCAL_NETWORK_PERMISSION) != PackageManager.PERMISSION_GRANTED
        if (needsPermission) localNetworkLauncher.launch(LOCAL_NETWORK_PERMISSION)
    }
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FeedTabRow(
                selectedTabIndex = state.selectedTabIndex,
                onTabSelected = { feedViewModel.onTabSelected(it) }
            )
            // Tres casos: cargando, error o contenido
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.errorMessage ?: "",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        TextButton(onClick = { feedViewModel.getAllPosts() }) {
                            Text(text = stringResource(R.string.feed_retry))
                        }
                    }
                }
                else -> FeedScreenContent(
                    // Variables de estado (Datos)
                    mockPosts = state.posts,

                    // Navegación y Eventos
                    onProductClick = onProductClick
                )
            }
        }
    }
}

private const val LOCAL_NETWORK_PERMISSION = "android.permission.ACCESS_LOCAL_NETWORK"
