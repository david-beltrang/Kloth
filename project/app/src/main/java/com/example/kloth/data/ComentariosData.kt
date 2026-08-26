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
            avatarRes = R.drawable.user1,
            commentText = "Totalmente de acuerdo, la calidad del abrigo es impresionante.",
            likesCount = 3
        ),
        ComentarioReviewData(
            id = "c2",
            authorName = "Lucas Méndez",
            timeAgo = "Hace 12h",
            avatarRes = R.drawable.user2,
            commentText = "¿Cómo te quedó de los hombros? Estoy dudando entre S y M.",
            likesCount = 1
        ),
        ComentarioReviewData(
            id = "c3",
            authorName = "Violetta Ávila",
            timeAgo = "Hace 4h",
            avatarRes = R.drawable.profile,
            commentText = "Me encanta pero siento que es muy delicada para usar en un dia casual.",
            likesCount = 3
        ),
        ComentarioReviewData(
            id = "c4",
            authorName = "Carmen Odiosa",
            timeAgo = "Hace 1 día",
            avatarRes = R.drawable.user3,
            commentText = "Esta fea",
            likesCount = 0
        )
    )
}