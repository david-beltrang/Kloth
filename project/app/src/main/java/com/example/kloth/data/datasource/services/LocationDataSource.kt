package com.example.kloth.data.datasource.services

import com.example.kloth.data.local.Coordinates

// Contrato para obtener la ubicación actual del dispositivo
interface LocationDataSource {
    suspend fun getCurrentLocation(): Coordinates?
}
