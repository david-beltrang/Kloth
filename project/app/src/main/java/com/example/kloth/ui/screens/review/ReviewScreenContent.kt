package com.example.kloth.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.data.local.ReviewData
import com.example.kloth.ui.screens.review.components.CommentInputField
import com.example.kloth.ui.screens.review.components.CommentItem
import com.example.kloth.ui.screens.review.components.MainReviewItem

@Composable
fun ReviewScreenContent(
    resena: ReviewData,
    isOwnReview: Boolean = false,
    isDeletingReview: Boolean = false,
    deleteReviewError: String? = null,
    currentUserImageUrl: String?,
    nuevoComentarioTexto: String,
    ordenMasRecientes: Boolean,
    yaComento: Boolean,
    onTextoComentarioChange: (String) -> Unit,
    onEnviarComentario: () -> Unit,
    onLikeResena: () -> Unit,
    onDeleteReview: () -> Unit = {},
    onLikeComentario: (String) -> Unit,
    onEliminarComentario: (String) -> Unit,
    onToggleOrden: () -> Unit,
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { if (!isDeletingReview) showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.review_delete_title)) },
            text = {
                Text(deleteReviewError ?: stringResource(R.string.review_delete_confirmation))
            },
            confirmButton = {
                TextButton(onClick = onDeleteReview, enabled = !isDeletingReview) {
                    if (isDeletingReview) CircularProgressIndicator()
                    else Text(stringResource(R.string.review_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = false },
                    enabled = !isDeletingReview
                ) { Text(stringResource(R.string.close)) }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                MainReviewItem(
                    resena = resena,
                    onLikeClick = onLikeResena,
                    onBackClick = onBackClick,
                    onAuthorClick = onUserClick,
                    canDelete = isOwnReview,
                    isDeleting = isDeletingReview,
                    onDeleteClick = { showDeleteConfirmation = true }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.label_comments_count, resena.comentariosList.size),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (ordenMasRecientes) {
                                stringResource(R.string.label_order_recent)
                            } else {
                                stringResource(R.string.label_order_old)
                            },
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.clickable { onToggleOrden() }
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }

            items(resena.comentariosList, key = { it.id }) { comentario ->
                CommentItem(
                    comentario = comentario,
                    onLikeClick = { onLikeComentario(comentario.id) },
                    onDeleteClick = { onEliminarComentario(comentario.id) },
                    onAuthorClick = onUserClick
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = 60.dp)
                )
            }
        }

        deleteReviewError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        CommentInputField(
            profileImageUrl = currentUserImageUrl,
            texto = nuevoComentarioTexto,
            onTextoChange = onTextoComentarioChange,
            onSendClick = onEnviarComentario,
            deshabilitado = yaComento
        )
    }
}
