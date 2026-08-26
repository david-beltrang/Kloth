package com.example.kloth.ui.screens.review

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.kloth.R
import com.example.kloth.data.ResenasDataLocal
import com.example.kloth.data.ReviewData
import com.example.kloth.ui.screens.review.components.CommentInputField
import com.example.kloth.ui.screens.review.components.CommentItem
import com.example.kloth.ui.screens.review.components.MainReviewItem
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun ReviewScreenContent(
    resena: ReviewData,
    nuevoComentarioTexto: String,
    ordenMasRecientes: Boolean,
    onTextoComentarioChange: (String) -> Unit,
    onEnviarComentario: () -> Unit,
    onLikeResena: () -> Unit,
    onLikeComentario: (String) -> Unit,
    onEliminarComentario: (String) -> Unit,
    onToggleOrden: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val yaComento = resena.comentariosList.any { it.isMyComment }

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
                    onBackClick = onBackClick
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
                    onDeleteClick = { onEliminarComentario(comentario.id) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = 60.dp)
                )
            }
        }

        CommentInputField(
            texto = nuevoComentarioTexto,
            onTextoChange = onTextoComentarioChange,
            onSendClick = onEnviarComentario,
            deshabilitado = yaComento
        )
    }
}

