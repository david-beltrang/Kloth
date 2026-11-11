package com.example.kloth.ui.screens.createReview

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CreateReviewScreen(
    productId: String,
    onBackClick: () -> Unit,
    onReviewSubmitted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Launcher que pide ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION juntos (Android 12+)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        viewModel.onLocationPermissionResult(granted)
    }

    // Solicitar la ubicación al entrar en la pantalla evita que una review nueva
    // se guarde sin coordenadas por no haber activado manualmente el switch.
    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            viewModel.onIncludeLocationChanged(true)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Carga de datos al entrar en la composición usando el ID recibido
    LaunchedEffect(productId) {
        viewModel.loadProductData(productId)
    }

    // Cuando la reseña se guarda, se vuelve al detalle
    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            onReviewSubmitted()
            viewModel.onSubmittedHandled()
        }
    }

    CreateReviewScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSubmitClick = { viewModel.submitReview() },
        onRatingSelected = { viewModel.onRatingSelected(it) },
        onReviewTextChanged = { viewModel.onReviewTextChanged(it) },
        onIncludeLocationChanged = { enabled ->
            if (enabled) {
                // Verificar si ya tiene permiso antes de lanzar el launcher
                val fineGranted = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                val coarseGranted = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                if (fineGranted || coarseGranted) {
                    viewModel.onIncludeLocationChanged(true)
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            } else {
                viewModel.onIncludeLocationChanged(false)
            }
        },
        modifier = modifier
    )
}
