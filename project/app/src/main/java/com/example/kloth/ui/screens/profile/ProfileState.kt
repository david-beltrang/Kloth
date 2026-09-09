package com.example.kloth.ui.screens.profile

data class ProfileState(
    val selectedTabIndex: Int = 0,
    val navigate: Boolean = false,
    val email: String = ""
)
