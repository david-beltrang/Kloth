package com.example.kloth.data

import androidx.compose.ui.graphics.Color

data class PostItem(
    val id: String,
    val creatorName: String,
    val timeAgo: String,
    val avatarUrl: String,
    val postImageUrl: String,
    val title: String,
    val rating: String,
    val reviewCount: Int,
    val comments: String,
    val description: String,
    val tags: List<String>,
    val category: String,
    val categoryColor: Color
)
