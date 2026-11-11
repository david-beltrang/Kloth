package com.example.kloth.ui.screens.tryOn.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.kloth.R
import com.example.kloth.data.local.ModelPic
import com.example.kloth.ui.screens.tryOn.TryOnPhase
import com.example.kloth.ui.screens.tryOn.TryOnState

@Composable
fun TryOnDialog(
    state: TryOnState,
    onDismiss: () -> Unit,
    onSelectBaseModelPic: (String) -> Unit,
    onRetryClick: () -> Unit,
    onGenerateClick: () -> Unit,
    onUploadBasePicClick: () -> Unit,
    onContinueClick: (String) -> Unit
) {
    if (!state.isDialogVisible) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .heightIn(min = 360.dp, max = 760.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.try_on_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                    }
                }

                when (state.phase) {
                    TryOnPhase.GENERATING -> GeneratingContent()
                    TryOnPhase.RESULT -> ResultContent(
                        imageUrl = state.resultImageUrl,
                        modelPicId = state.resultModelPicId,
                        onContinueClick = onContinueClick
                    )
                    else -> SelectionContent(
                        state = state,
                        onSelectBaseModelPic = onSelectBaseModelPic,
                        onRetryClick = onRetryClick,
                        onGenerateClick = onGenerateClick,
                        onUploadBasePicClick = onUploadBasePicClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.SelectionContent(
    state: TryOnState,
    onSelectBaseModelPic: (String) -> Unit,
    onRetryClick: () -> Unit,
    onGenerateClick: () -> Unit,
    onUploadBasePicClick: () -> Unit
) {
    Text(
        text = stringResource(R.string.try_on_select_model_pic),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(12.dp))

    when {
        state.isLoadingModelPics -> Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        state.errorMessage != null -> Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
        state.baseModelPics.isEmpty() -> EmptyBasePicsContent(onUploadBasePicClick)
        else -> LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.baseModelPics, key = { it.id }) { pic ->
                BaseModelPicRow(
                    modelPic = pic,
                    selected = state.selectedBaseModelPicId == pic.id,
                    onSelected = { onSelectBaseModelPic(pic.id) }
                )
            }
        }
    }

    if (state.phase == TryOnPhase.ERROR && state.baseModelPics.isEmpty()) {
        TextButton(onClick = onRetryClick, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(stringResource(R.string.feed_retry))
        }
    }

    if (state.baseModelPics.isNotEmpty()) {
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onGenerateClick,
            enabled = state.selectedBaseModelPicId != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.try_on_button))
        }
    }
}

@Composable
private fun ColumnScope.EmptyBasePicsContent(onUploadBasePicClick: () -> Unit) {
    Column(
        modifier = Modifier.weight(1f).fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.try_on_no_model_pics),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onUploadBasePicClick) {
            Text(stringResource(R.string.model_pics_upload))
        }
    }
}

@Composable
private fun BaseModelPicRow(
    modelPic: ModelPic,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = modelPic.imageUrl,
                contentDescription = modelPic.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = modelPic.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            Checkbox(checked = selected, onCheckedChange = { onSelected() })
        }
    }
}

@Composable
private fun ColumnScope.GeneratingContent() {
    Column(
        modifier = Modifier.weight(1f).fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.try_on_generating))
    }
}

@Composable
private fun ColumnScope.ResultContent(
    imageUrl: String?,
    modelPicId: String?,
    onContinueClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.weight(1f).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.try_on_result_content_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }
        Button(
            onClick = { modelPicId?.let(onContinueClick) },
            enabled = modelPicId != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.try_on_continue))
        }
    }
}
