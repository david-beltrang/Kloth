package com.example.kloth.ui.screens.review

import com.example.kloth.data.ResenasDataLocal
import com.example.kloth.data.ReviewData

data class ReviewState(
    val resena: ReviewData = ResenasDataLocal.resenaDetalleSeleccionada,
    val nuevoComentarioTexto: String = "",
    val ordenMasRecientes: Boolean = true
) {
    val yaComento: Boolean
        get() = resena.comentariosList.any { it.isMyComment }
}