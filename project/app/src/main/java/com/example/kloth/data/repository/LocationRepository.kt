package com.example.kloth.data.repository

import com.example.kloth.data.datasource.services.LocationDataSource
import com.example.kloth.data.local.Coordinates
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

private const val COORDINATE_DECIMALS = 3
private val SCALE = Math.pow(10.0, COORDINATE_DECIMALS.toDouble())

// Obtiene la ubicación y la redondea 3 decimales (~110 m)
@Singleton
class LocationRepository @Inject constructor(
    private val locationDataSource: LocationDataSource
) {
    suspend fun getCurrentCoordinates(): Result<Coordinates> {
        val ubicacion = locationDataSource.getCurrentLocation()
            ?: return Result.failure(Exception("No se pudo obtener la ubicación"))
        val rounded = Coordinates(
            latitude = (ubicacion.latitude * SCALE).roundToInt() / SCALE,
            longitude = (ubicacion.longitude * SCALE).roundToInt() / SCALE
        )
        return Result.success(rounded)
    }
}
