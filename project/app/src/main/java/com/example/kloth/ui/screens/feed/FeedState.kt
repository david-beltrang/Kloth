package com.example.kloth.ui.screens.feed

import com.example.kloth.data.PostItem

data class FeedState(
    val posts: List<PostItem> = emptyList(),
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false
)
