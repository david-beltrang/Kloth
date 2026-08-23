package com.example.kloth.data

import androidx.annotation.DrawableRes
import com.example.kloth.R

data class ComentarioReviewData(
    val id: String,
    val authorName: String,
    val timeAgo: String,
    @DrawableRes val avatarRes: Int,
    val commentText: String,
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val isMyComment: Boolean = false
)

object ComentariosDataLocal {
    val comentariosEjemplo = listOf(
        ComentarioReviewData(
            id = "c1",
            authorName = "Sofia Rossi",
            timeAgo = "Hace 1 día",
            avatarRes = R.drawable.abrigo_negro,
            commentText = "Totalmente de acuerdo, la calidad del abrigo es impresionante.",
            likesCount = 3
        ),
        ComentarioReviewData(
            id = "c2",
            authorName = "Lucas Méndez",
            timeAgo = "Hace 12h",
            avatarRes = R.drawable.abrigo_negro,
            commentText = "¿Cómo te quedó de los hombros? Estoy dudando entre S y M.",
            likesCount = 1
        )
    )
}