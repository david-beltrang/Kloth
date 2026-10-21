package com.example.kloth.ui.screens.editProfile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
// ViewModel para gestionar el estado de la edición de perfil.
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState: StateFlow<EditProfileState> = _uiState.asStateFlow()

    init {
        // Inicializar con la URL actual del usuario
        _uiState.update { 
            it.copy(
                profileImageUrl = authRepository.currentUser?.photoUrl?.toString(),
                email = authRepository.currentUser?.email.orEmpty()
            )
        }
        loadProfile()
    }

    // Llena el formulario con los datos actuales de users/{uid}
    private fun loadProfile() {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            userRepository.getUserById(userId).onSuccess { user ->
                _uiState.update {
                    it.copy(
                        username = user.username.orEmpty(),
                        bio = user.bio.orEmpty(),
                        location = user.location.orEmpty(),
                        website = user.website.orEmpty(),
                        email = user.email.ifBlank { it.email },
                        profileImageUrl = user.profileImageUrl?.takeIf { url -> url.isNotBlank() }
                            ?: it.profileImageUrl
                    )
                }
            }
        }
    }

    // Guarda los cambios en Firestore. El email no se guarda: vive en Firebase Auth
    fun saveProfile() {
        val state = _uiState.value
        val userId = authRepository.currentUser?.uid ?: return
        if (state.username.isBlank() || state.isSaving) return

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val result = userRepository.updateProfile(
                userId = userId,
                username = state.username,
                biography = state.bio,
                location = state.location,
                website = state.website
            )
            _uiState.update { it.copy(isSaving = false, isSaved = result.isSuccess) }
        }
    }

    fun onSavedConsumed() {
        _uiState.update { it.copy(isSaved = false) }
    }

    // Funcion para subir imagen
    fun onImagePicked(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(profileImageUrl = uri.toString(), isLoading = true) }

            val userId = authRepository.currentUser?.uid.toString()
            val result = storageRepository.uploadProfileImage(uri)

            if (result.isSuccess) {
                val url = result.getOrThrow()
                userRepository.updateProfileImage(userId,url)
                    .onFailure { Log.e("EditProfileViewModel", "Error al actualizar la imagen de perfil") }
                _uiState.update { it.copy(profileImageUrl = url, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

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
