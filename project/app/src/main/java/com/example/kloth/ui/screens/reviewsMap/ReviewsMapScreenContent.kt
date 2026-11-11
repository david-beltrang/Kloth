package com.example.kloth.ui.screens.reviewsMap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.data.local.ReviewMapPin
import com.example.kloth.ui.screens.reviewsMap.components.ReviewPinSheet
import com.example.kloth.ui.screens.reviewsMap.components.ReviewsMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsMapScreenContent(
    uiState: ReviewsMapState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onPinSelected: (String) -> Unit,
    onPinDismissed: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    // El mapa se recibe como slot para poder previsualizar la pantalla sin instanciar MapView.
    mapContent: @Composable (List<ReviewMapPin>, (String) -> Unit) -> Unit = { pins, onPinClick ->
        ReviewsMap(pins = pins, onPinClick = onPinClick)
    }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("reviewsMapScreen")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = stringResource(R.string.map_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(onClick = onRefreshClick) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = stringResource(R.string.map_refresh),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // El mapa vive en la composición para que su MapView no se destruya al recargar
            mapContent(uiState.pins, onPinSelected)

            when {
                uiState.isLoading -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                uiState.errorMessage != null -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.errorMessage,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        TextButton(onClick = onRetryClick) {
                            Text(text = stringResource(R.string.map_retry))
                        }
                    }
                }

                uiState.pins.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.map_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }

    // El sheet vive en el estado (selectedPin)
    // Si se deja de seleccionar vuelve a null.
    uiState.selectedPin?.let { pin ->
        ModalBottomSheet(
            onDismissRequest = onPinDismissed,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            ReviewPinSheet(
                pin = pin,
                onOpenReview = {
                    onPinDismissed()
                    onReviewClick(pin.reviewId)
                }
            )
        }
    }
}
