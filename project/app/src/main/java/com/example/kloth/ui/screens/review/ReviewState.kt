package com.example.kloth.ui.screens.review

import com.example.kloth.data.local.ReviewData

data class ReviewState(
    val resena: ReviewData,
    val nuevoComentarioTexto: String = "",
    val ordenMasRecientes: Boolean = true
) {
    val yaComento: Boolean
        get() = resena.comentariosList.any { it.isMyComment }
}