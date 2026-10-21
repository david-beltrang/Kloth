package com.example.kloth.data.local

import androidx.annotation.DrawableRes
import com.example.kloth.R

data class ReviewData(
    val id: String,
    val authorName: String,
    val timeAgo: String = "",
    @get:DrawableRes val avatarRes: Int,
    val avatarUrl: String? = null, // foto del autor desde el backend; si es null se usa avatarRes
    val rating: Float,
    val reviewText: String,
    val likesCount: Int = 0,
    val liked: Boolean = false,
    val commentsCountText: String = "0",
    val comentariosList: List<ComentarioReviewData> = emptyList()
)
