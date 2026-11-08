package com.example.kloth.ui.screens.notification

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.R
import com.example.kloth.data.local.TipoNotificacion
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.NotificationRepository
import com.example.kloth.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Coordina la lista de notificaciones, el estado de lectura y la accion de seguir.
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationState())
    val uiState: StateFlow<NotificationState> = _uiState
    private var notificationsJob: Job? = null

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        val userId = authRepository.currentUser?.uid
        if (userId.isNullOrBlank()) {
            _uiState.update { it.copy(isLoading = false, notificaciones = emptyList()) }
            return
        }

        notificationsJob?.cancel()
        notificationsJob = viewModelScope.launch {
            // El listener entrega la carga inicial y las actualizaciones posteriores de Firestore.
            var markedInitialNotificationsRead = false
            notificationRepository.observeNotifications(userId)
                .onStart { _uiState.update { it.copy(isLoading = true, errorMessage = null) } }
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = context.getString(R.string.notifications_load_error)
                        )
                    }
                }
                .collect { notifications ->
                    val hasFollowerNotifications = notifications.any {
                        it.tipo == TipoNotificacion.NUEVO_SEGUIDOR && it.actorId.isNotBlank()
                    }
                    val followingIds = if (hasFollowerNotifications) {
                        userRepository.getFollowingIds(userId).getOrDefault(emptySet())
                    } else emptySet()
                    val notificationsWithFollowState = notifications.map { notification ->
                        if (notification.tipo == TipoNotificacion.NUEVO_SEGUIDOR && notification.actorId.isNotBlank()) {
                            notification.copy(estaSiguiendo = notification.actorId in followingIds)
                        } else notification
                    }
                    _uiState.update {
                        it.copy(
                            notificaciones = notificationsWithFollowState,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    if (!markedInitialNotificationsRead) {
                        markedInitialNotificationsRead = true
                        // Al abrir la pantalla se marcan como leidas las notificaciones no leidas iniciales.
                        runCatching {
                            notificationRepository.markNotificationsRead(
                                userId,
                                notifications.filter { it.noLeida }.map { it.id }
                            )
                        }
                    }
                }
        }
    }

    fun onToggleSeguir(notificationId: String) {
        val userId = authRepository.currentUser?.uid ?: return
        val notification = _uiState.value.notificaciones.firstOrNull { it.id == notificationId } ?: return
        if (notification.actorId.isBlank()) return

        viewModelScope.launch {
            userRepository.followOrUnfollowUser(userId, notification.actorId)
                .onSuccess { followState ->
                    _uiState.update { state ->
                        state.copy(
                            notificaciones = state.notificaciones.map {
                                if (it.id == notificationId) it.copy(estaSiguiendo = followState.isFollowing) else it
                            }
                        )
                    }
                }
        }
    }
}
