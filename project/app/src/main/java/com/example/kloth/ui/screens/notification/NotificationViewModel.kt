package com.example.kloth.ui.screens.notification

import androidx.lifecycle.ViewModel
import com.example.kloth.data.NotificacionesDataLocal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NotificationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationState())
    val uiState: StateFlow<NotificationState> = _uiState

    //Init paea cargar todas las notificaciones, no necesita ID
    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        _uiState.update { it.copy(isLoading = true) }

        val notificaciones = NotificacionesDataLocal.notificacionesEjemplo

        _uiState.update {
            it.copy(
                notificaciones = notificaciones,
                isLoading = false
            )
        }
    }

    fun onToggleSeguir(id: String) {
        _uiState.update { currentState ->
            currentState.copy(
                notificaciones = currentState.notificaciones.map { notificacion ->
                    if (notificacion.id == id) {
                        notificacion.copy(estaSiguiendo = !notificacion.estaSiguiendo)
                    } else {
                        notificacion
                    }
                }
            )
        }
    }
}
