package com.example.kloth.ui.screens.profile

import android.net.Uri
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState(
        email = authRepository.currentUser?.email ?: "",
        profileImageUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
    ))

    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val userId = authRepository.currentUser?.uid ?: return@launch
            try {
                val userDto = userRepository.getUserById(userId)
                _uiState.update {
                    it.copy(
                        username = userDto.username.takeIf { name -> name.isNotBlank() },
                        bio = userDto.biography,
                        location = null,
                        website = null,
                        email = userDto.email.ifBlank { it.email },
                        profileImageUrl = userDto.imageURL?.takeIf { url -> url.isNotBlank() } ?: it.profileImageUrl
                    )
                }
            } catch (e: Exception) {
                // Ignore or log error
            }
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun logOutButtonPressed() {
        authRepository.signOut()
        _uiState.update { it.copy(navigate = true) }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigate = false) }
    }

    fun uploadImageToFirebase(uri: Uri){
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess){
                _uiState.update { it.copy(profileImageUrl = result.getOrNull())}
            }
        }
    }
}
