package com.example.kloth.ui.screens.review

import com.example.kloth.R
import com.example.kloth.data.local.ReviewData

data class ReviewState(
    // Vacía mientras se carga la reseña desde Firestore
    val resena: ReviewData = ReviewData(
        id = "",
        userId = "",
        authorName = "",
        avatarRes = R.drawable.profile,
        rating = 0f,
        reviewText = ""
    ),
    val nuevoComentarioTexto: String = "",
    val ordenMasRecientes: Boolean = true,
    val currentUserId: String = ""
) {
    val yaComento: Boolean
        get() = resena.comentariosList.any { it.isMyComment }
}