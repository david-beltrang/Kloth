package com.example.kloth.data

/**
 * Sesión local. La app conserva Firebase para autenticación, pero el backend
 * Express aún no expone login, así que el usuario actual se mantiene fijo.
 */
object Session {
    const val CURRENT_USER_ID: Int = 1
}
