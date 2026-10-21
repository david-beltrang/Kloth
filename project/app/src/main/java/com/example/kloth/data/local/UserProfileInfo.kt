package com.example.kloth.data.local


data class UserProfileInfo(
    val id: String,
    val username: String?,
    val email: String,
    val bio: String?,
    val location: String?,
    val website: String?,
    val profileImageUrl: String?,
    val followersCount: Int = 0,
    val followingCount: Int = 0
)

data class UserFollowState(
    val isFollowing: Boolean,
    val followersCount: Int
)
