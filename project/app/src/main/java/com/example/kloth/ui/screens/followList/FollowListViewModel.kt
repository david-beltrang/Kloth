package com.example.kloth.ui.screens.followList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FollowListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FollowListState())
    val uiState = _uiState.asStateFlow()

    fun load(userId: String, listType: String) {
        viewModelScope.launch {
            val currentUserId = authRepository.currentUser?.uid
            _uiState.value = FollowListState(
                isLoading = true,
                ownerUserId = userId,
                currentUserId = currentUserId,
                listType = listType
            )
            userRepository.getFollowedUsers(userId, listType).onSuccess { users ->
                val followingIds = currentUserId?.let {
                    userRepository.getFollowingIds(it).getOrDefault(emptySet())
                } ?: emptySet()
                _uiState.value = FollowListState(
                    users = users,
                    isLoading = false,
                    ownerUserId = userId,
                    currentUserId = currentUserId,
                    listType = listType,
                    followingUserIds = followingIds
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun onUserAction(userId: String) {
        val currentState = _uiState.value
        val currentUserId = currentState.currentUserId ?: return
        val isRemovingFollower = currentState.isOwnList && currentState.listType == "followers"
        if (currentState.isOwnList && currentState.listType == "following") {
            unfollow(userId)
            return
        }

        viewModelScope.launch {
            if (isRemovingFollower) {
                userRepository.removeFollower(currentUserId, userId).onSuccess {
                    _uiState.update { state ->
                        state.copy(users = state.users.filterNot { it.id == userId })
                    }
                }
                return@launch
            }

            userRepository.followOrUnfollowUser(currentUserId, userId).onSuccess { followState ->
                _uiState.update { state ->
                    val shouldRemoveRow = state.isOwnList && state.listType == "following" &&
                        !followState.isFollowing
                    val updatedFollowingIds = if (followState.isFollowing) {
                        state.followingUserIds + userId
                    } else {
                        state.followingUserIds - userId
                    }
                    state.copy(
                        users = if (shouldRemoveRow) state.users.filterNot { it.id == userId } else state.users,
                        followingUserIds = updatedFollowingIds
                    )
                }
            }
        }
    }

    fun unfollow(userId: String) {
        val currentUserId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            userRepository.isFollowing(currentUserId, userId).onSuccess { isFollowing ->
                if (isFollowing) {
                    userRepository.followOrUnfollowUser(currentUserId, userId).onSuccess { followState ->
                        if (!followState.isFollowing) {
                            _uiState.value = _uiState.value.copy(
                                users = _uiState.value.users.filterNot { it.id == userId }
                            )
                        }
                    }
                }
            }
        }
    }
}
