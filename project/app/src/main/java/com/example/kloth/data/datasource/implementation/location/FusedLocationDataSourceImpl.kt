package com.example.kloth.data.datasource.implementation.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.kloth.data.datasource.services.LocationDataSource
import com.example.kloth.data.local.Coordinates
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

// Implementación con Fused Location Provider. await() es el mismo de kotlinx.coroutines.tasks
// que ya usa Firestore en el proyecto.
class FusedLocationDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationDataSource {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Coordinates? {
        return try {
            val client = LocationServices.getFusedLocationProviderClient(context)
            // 1. Intentar lastLocation primero (es instantáneo, sin demora ni espera de GPS)
            val lastKnown = client.lastLocation.await()
            if (lastKnown != null) {
                return Coordinates(lastKnown.latitude, lastKnown.longitude)
            }

            // 2. Si lastLocation es null, dar tiempo suficiente a que el GPS obtenga una lectura.
            withTimeoutOrNull(10_000) {
                val location = client
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .await()
                location?.let { Coordinates(it.latitude, it.longitude) }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w("FusedLocation", "No se pudo obtener la ubicación", e)
            null
        }
    }
}
