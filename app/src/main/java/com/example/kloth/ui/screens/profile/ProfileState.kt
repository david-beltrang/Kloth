package com.example.kloth.ui.screens.profile

import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ReviewData

data class ProfileState(
    val selectedTabIndex: Int = 0,
    val navigate: Boolean = false,
    val email: String = "",
    val profileImageUrl: String? = null,
    val profileUserName: String? = null,
    val isCurrentUser: Boolean = true,
    val userArticles: List<PostItem> = emptyList(),
    val userReviews: List<ReviewData> = emptyList(),
    val isLoadingArticles: Boolean = false,
    val isLoadingReviews: Boolean = false
)
