package com.example.kloth.ui.screens.review

import com.example.kloth.data.local.ResenasDataLocal
import com.example.kloth.data.local.ReviewData

data class ReviewState(
    val resena: ReviewData = ResenasDataLocal.resenaDetalleSeleccionada,
    val nuevoComentarioTexto: String = "",
    val ordenMasRecientes: Boolean = true,
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val errorMessage: String? = null
) {
    val yaComento: Boolean
        get() = resena.comentariosList.any { it.isMyComment }
}
