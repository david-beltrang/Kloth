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
    val commentsText: String = ""
)

val defaultReviewsList = listOf(
    ReviewData(
        id = "1",
        authorName = "Elena V. Costanza",
        timeAgo = "Hace 2 días",
        avatarRes = R.drawable.abrigo_negro,
        rating = 5.0f,
        reviewText = "La calidad de la tela es excepcional. Es ligero pero increíblemente cálido. Definitivamente una inversión que vale cada céntimo para cualquier armario profesional.",
        likesCount = 24,
        commentsText = "2"
    ),
    ReviewData(
        id = "2",
        authorName = "Marcus Thorne",
        timeAgo = "Hace 1 semana",
        avatarRes = R.drawable.abrigo_negro,
        rating = 4.0f,
        reviewText = "El corte es perfecto, aunque las mangas son ligeramente más largas de lo esperado. Aún así, la construcción es de nivel artesanal.",
        likesCount = 12,
        commentsText = "Responder"
    )
)