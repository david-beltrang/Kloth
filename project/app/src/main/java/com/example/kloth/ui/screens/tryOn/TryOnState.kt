package com.example.kloth.ui.screens.tryOn

import com.example.kloth.data.local.ModelPic

enum class TryOnPhase {
    SELECTING,
    GENERATING,
    RESULT,
    ERROR
}

data class TryOnState(
    val isDialogVisible: Boolean = false,
    val publicationId: String? = null,
    val garmentImageUrl: String? = null,
    val baseModelPics: List<ModelPic> = emptyList(),
    val selectedBaseModelPicId: String? = null,
    val isLoadingModelPics: Boolean = false,
    val phase: TryOnPhase = TryOnPhase.SELECTING,
    val resultImageUrl: String? = null,
    val resultModelPicId: String? = null,
    val errorMessage: String? = null
)
