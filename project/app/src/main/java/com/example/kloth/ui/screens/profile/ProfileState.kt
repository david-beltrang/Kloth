package com.example.kloth.ui.screens.profile

data class ProfileState(
    val selectedTabIndex: Int = 0,
    val navigate: Boolean = false,
    val email: String = "",
    val username: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val website: String? = null,
    val profileImageUrl: String? = null
)
