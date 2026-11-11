package com.example.kloth.data.dto

import com.example.kloth.data.local.TryOnResult

data class TryOnResultDto(
    val modelPicId: String,
    val imageUrl: String
) {
    fun toTryOnResult(): TryOnResult = TryOnResult(
        modelPicId = modelPicId,
        imageUrl = imageUrl
    )
}
