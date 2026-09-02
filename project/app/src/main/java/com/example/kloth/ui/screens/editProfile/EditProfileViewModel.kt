package com.example.kloth.ui.screens.editProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kloth.ui.screens.editProfile.EditProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ViewModel para gestionar el estado de la edición de perfil.
class EditProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState: StateFlow<EditProfileState> = _uiState.asStateFlow()

    // Funcion para actualizar el Username
    fun updateUsername(input: String) {
        Log.d("EditProfileViewModel", _uiState.value.username.length.toString())
        _uiState.update { it.copy(username = input) }
    }

    // Funcion para actualizar el Bio
    fun updateBio(input: String) {
        Log.d("EditProfileViewModel", _uiState.value.bio.length.toString())
        _uiState.update { it.copy(bio = input) }
    }

    // Funcion para actualizar el Email
    fun updateEmail(input: String) {
        Log.d("EditProfileViewModel", _uiState.value.email.length.toString())
        _uiState.update { it.copy(email = input) }
    }

    // Funcion para actualizar la Ubicacion
    fun updateLocation(input: String) {
        Log.d("EditProfileViewModel", _uiState.value.location.length.toString())
        _uiState.update { it.copy(location = input) }
    }

    // Funcion para actualizar la Web
    fun updateWebsite(input: String) {
        Log.d("EditProfileViewModel", _uiState.value.website.length.toString())
        _uiState.update { it.copy(website = input) }
    }
}
