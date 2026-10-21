package com.example.kloth.ui.screens.profile

import com.example.kloth.data.local.PostItem

data class ProfileState(
    val selectedTabIndex: Int = 0,
    val navigate: Boolean = false,
    val email: String = "",
    val username: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val website: String? = null,
    val profileImageUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val userArticles: List<PostItem> = emptyList(),
    val reviewedArticles: List<PostItem> = emptyList()
)
