package com.example.kloth.ui.screens.followList

import com.example.kloth.data.local.UserProfileInfo

data class FollowListState(
    val users: List<UserProfileInfo> = emptyList(),
    val isLoading: Boolean = true,
    val ownerUserId: String? = null,
    val currentUserId: String? = null,
    val listType: String = "following",
    val followingUserIds: Set<String> = emptySet()
) {
    val isOwnList: Boolean
        get() = ownerUserId != null && ownerUserId == currentUserId
}
