package com.example.kloth.ui.screens.review

import com.example.kloth.R
import com.example.kloth.data.local.ReviewData

data class ReviewState(
    val currentUserId: String? = null,
    val currentUserImageUrl: String? = null,
    val currentUserName: String = "Tú",
    // Vacía mientras se carga la reseña desde Firestore
    val resena: ReviewData = ReviewData(
        id = "",
        authorName = "",
        avatarRes = R.drawable.profile,
        rating = 0f,
        reviewText = ""
    ),
    val nuevoComentarioTexto: String = "",
    val ordenMasRecientes: Boolean = true,
    val isDeletingReview: Boolean = false,
    val reviewDeleted: Boolean = false,
    val deleteReviewError: String? = null
) {
    val yaComento: Boolean
        get() = resena.comentariosList.any { it.isMyComment }
}
