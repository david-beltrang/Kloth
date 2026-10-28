package com.example.kloth.ui.screens.feed

import com.example.kloth.data.local.PostItem

data class FeedState(
    val posts: List<PostItem> = emptyList(),
    val allPosts: List<PostItem> = emptyList(),
    val selectedTabIndex: Int = 1,
    val hasFollowing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
