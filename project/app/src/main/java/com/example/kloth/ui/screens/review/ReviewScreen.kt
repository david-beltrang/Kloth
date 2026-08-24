package com.example.kloth.ui.screens.review

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.data.ComentarioReviewData
import com.example.kloth.data.ResenasDataLocal

@Composable
fun ReviewScreen(
    onBackClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    var resenaState by remember { mutableStateOf(ResenasDataLocal.resenaDetalleSeleccionada) }
    var nuevoComentarioTexto by remember { mutableStateOf("") }
    var ordenMasRecientes by remember { mutableStateOf(true) }

    ReviewScreenContent(
        resena = resenaState,
        nuevoComentarioTexto = nuevoComentarioTexto,
        ordenMasRecientes = ordenMasRecientes,
        onTextoComentarioChange = { nuevoComentarioTexto = it },
        onEnviarComentario = {
            if (nuevoComentarioTexto.isNotBlank()) {
                val nuevoComentario = ComentarioReviewData(
                    id = "my_c_${System.currentTimeMillis()}",
                    authorName = "Tú",
                    timeAgo = "Ahora",
                    avatarRes = R.drawable.abrigo_negro,
                    commentText = nuevoComentarioTexto,
                    likesCount = 0,
                    isMyComment = true
                )
                val listaActualizada = if (ordenMasRecientes) {
                    listOf(nuevoComentario) + resenaState.comentariosList
                } else {
                    resenaState.comentariosList + nuevoComentario
                }
                resenaState = resenaState.copy(comentariosList = listaActualizada)
                nuevoComentarioTexto = ""
            }
        },
        onLikeResena = {
            resenaState = resenaState.copy(
                isLikedByMe = !resenaState.isLikedByMe,
                likesCount = if (resenaState.isLikedByMe) resenaState.likesCount - 1 else resenaState.likesCount + 1
            )
        },
        onLikeComentario = { id ->
            val comentariosActualizados = resenaState.comentariosList.map { c ->
                if (c.id == id) {
                    val nuevoMeGusta = !c.isLikedByMe
                    c.copy(
                        isLikedByMe = nuevoMeGusta,
                        likesCount = if (nuevoMeGusta) c.likesCount + 1 else c.likesCount - 1
                    )
                } else c
            }
            resenaState = resenaState.copy(comentariosList = comentariosActualizados)
        },
        onEliminarComentario = { id ->
            val comentariosActualizados = resenaState.comentariosList.filterNot { it.id == id }
            resenaState = resenaState.copy(comentariosList = comentariosActualizados)
        },
        onToggleOrden = {
            ordenMasRecientes = !ordenMasRecientes
            resenaState = resenaState.copy(comentariosList = resenaState.comentariosList.reversed())
        },
        onBackClick = onBackClick,
        contentPadding = contentPadding,
        modifier = modifier
    )
}