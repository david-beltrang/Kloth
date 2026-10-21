package com.example.kloth.ui.screens.followList

import com.example.kloth.data.local.UserProfileInfo

data class FollowListState(
    val users: List<UserProfileInfo> = emptyList(),
    val isLoading: Boolean = true
)
