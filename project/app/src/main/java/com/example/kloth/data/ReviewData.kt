package com.example.kloth.data

import androidx.annotation.DrawableRes
import com.example.kloth.R

data class ReviewData(
    val id: String,
    val authorName: String,
    val timeAgo: String,
    @DrawableRes val avatarRes: Int,
    val rating: Float,
    val reviewText: String,
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val commentsCountText: String = "0",
    val comentariosList: List<ComentarioReviewData> = emptyList()
)

object ResenasDataLocal {
    val defaultReviewsList = listOf(
        ReviewData(
            id = "1",
            authorName = "Elena V. Costanza",
            timeAgo = "Hace 2 días",
            avatarRes = R.drawable.user1,
            rating = 5.0f,
            reviewText = "La calidad de la tela es excepcional. Es ligero pero increíblemente cálido. Definitivamente una inversión que vale cada céntimo para cualquier armario profesional.",
            likesCount = 24,
            commentsCountText = "2",
            comentariosList = ComentariosDataLocal.comentariosEjemplo
        ),
        ReviewData(
            id = "2",
            authorName = "Marcus Thorne",
            timeAgo = "Hace 1 semana",
            avatarRes = R.drawable.user2,
            rating = 4.0f,
            reviewText = "El corte es perfecto, aunque las mangas son ligeramente más largas de lo esperado. Aún así, la construcción es de nivel artesanal.",
            likesCount = 12,
            commentsCountText = "0"
        )
    )

    val resenaDetalleSeleccionada = defaultReviewsList.first()
}