package com.example.kloth.ui.screens.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kloth.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Expone el numero de notificaciones no leidas para la insignia de navegacion.
@HiltViewModel
class NotificationBadgeViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount
    private var observeJob: Job? = null

    fun observeUserNotifications(userId: String?) {
        observeJob?.cancel()
        _unreadCount.value = 0
        if (userId.isNullOrBlank()) return

        observeJob = viewModelScope.launch {
            // El badge observa los mismos documentos de la pantalla y cuenta los que siguen sin leer.
            notificationRepository.observeNotifications(userId)
                .map { notifications -> notifications.count { it.noLeida } }
                .catch { emit(0) }
                .collect { _unreadCount.value = it }
        }
    }
}
