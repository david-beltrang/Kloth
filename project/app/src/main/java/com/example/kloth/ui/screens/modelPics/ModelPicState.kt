package com.example.kloth.ui.screens.modelPics

import com.example.kloth.data.local.ModelPic
import com.example.kloth.data.local.ModelPicType

data class ModelPicState(
    val modelPics: List<ModelPic> = emptyList(),
    val selectedFilter: ModelPicType = ModelPicType.BASE,
    val isLoading: Boolean = false,
    val isUploadingBaseModelPic: Boolean = false,
    val errorMessage: String? = null,
    val uploadErrorMessage: String? = null,
    val createdModelPicId: String? = null
) {
    val visibleModelPics: List<ModelPic>
        get() = modelPics.filter { it.type == selectedFilter }
}
