package com.example.kloth.ui.screens.editProfile

data class EditProfileState (
    val username: String = "",
    val bio: String = "",
    val email: String = "",
    val location: String = "",
    val website: String = "",
    val profileImageUrl: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
)