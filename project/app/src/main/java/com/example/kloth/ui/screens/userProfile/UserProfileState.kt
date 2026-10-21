package com.example.kloth.ui.screens.userProfile

import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.UserProfileInfo

data class UserProfileState(
    val user: UserProfileInfo? = null,
    val userArticles: List<PostItem> = emptyList(),
    val selectedTabIndex: Int = 0,
    val currentUserId: String = "",
    // Inicia en true para no mostrar "Usuario no encontrado" antes de la primera carga
    val isLoading: Boolean = true,
    val userNotFound: Boolean = false
)
