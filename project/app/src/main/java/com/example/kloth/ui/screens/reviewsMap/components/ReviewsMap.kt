package com.example.kloth.ui.screens.reviewsMap.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.kloth.R
import com.example.kloth.data.datasource.implementation.location.FusedLocationDataSourceImpl
import com.example.kloth.data.local.ReviewMapPin
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory.iconAllowOverlap
import org.maplibre.android.style.layers.PropertyFactory.iconAnchor
import org.maplibre.android.style.layers.PropertyFactory.iconIgnorePlacement
import org.maplibre.android.style.layers.PropertyFactory.iconImage
import org.maplibre.android.style.layers.PropertyFactory.iconSize
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

// Único archivo de la feature que conoce MapLibre. El resto del código trabaja con ReviewMapPin

// Estilos de OpenFreeMap: positron para modo claro, dark para modo oscuro.
private const val MAP_STYLE_LIGHT_URL = "https://tiles.openfreemap.org/styles/positron"
private const val MAP_STYLE_DARK_URL  = "https://tiles.openfreemap.org/styles/dark"

// IDs internos del mapa para fuente, capa e imagen del pin
private const val PIN_SOURCE_ID       = "review_pins_source"
private const val PIN_LAYER_ID        = "review_pins_layer"
private const val PIN_IMAGE_ID        = "review_pin_hanger"

// Propiedad GeoJSON que identifica cada pin con su reviewId
private const val REVIEW_ID_PROPERTY  = "reviewId"

// Parámetros de animación y zoom de cámara.
private const val CAMERA_ANIMATION_MS = 600
private const val BOUNDS_PADDING_PX   = 140
private const val SINGLE_PIN_ZOOM     = 14.0
private const val DEFAULT_ZOOM        = 11.0
private const val MY_LOCATION_ZOOM    = 15.0

// Centro inicial cuando no hay pines cargados (Bogotá)
private val DEFAULT_CENTER = LatLng(4.7110, -74.0721)

// Devuelve la URL del estilo según el tema activo.
private fun mapStyleUrl(darkTheme: Boolean) =
    if (darkTheme) MAP_STYLE_DARK_URL else MAP_STYLE_LIGHT_URL

// Devuelve el drawable del pin que contrasta con el fondo del mapa activo
@DrawableRes
private fun pinDrawableRes(darkTheme: Boolean) =
    if (darkTheme)
        R.drawable.ic_hanger_pin_dark
    else
        R.drawable.ic_hanger_pin_light

@Composable
fun ReviewsMap(
    pins: List<ReviewMapPin>,
    onPinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    // Misma fuente que KlothTheme. El default evita tocar a ReviewsMapScreenContent
    // y permite forzar el modo en previews o tests.
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // MapView reutilizable: Se crea una sola vez
    val stableMapView = remember(context) { MapView(context) }

    // rememberUpdatedState evita capturar valores stale en lambdas asíncronas.
    val currentPins by rememberUpdatedState(pins)
    val currentOnPinClick by rememberUpdatedState(onPinClick)

    // Estilo activo de MapLibre
    var style by remember { mutableStateOf<Style?>(null) }

    // Cuando la referencia al mapa este lista se hace el LaunchedEffect
    var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }

    // Lista de pines ya renderizados, evita reanimar la cámara en cada recomposición
    var renderedPins by remember { mutableStateOf<List<ReviewMapPin>?>(null) }

    // Se ejecuta al obtener el mapa o cuando cambia el tema
    LaunchedEffect(mapLibreMap, darkTheme) {
        val map = mapLibreMap ?: return@LaunchedEffect
        // Bloquea el bloque update mientras el nuevo estilo no está listo
        style = null
        map.setStyle(Style.Builder().fromUri(mapStyleUrl(darkTheme))) { loadedStyle ->
            Log.d("ReviewsMap", "Estilo cargado: darkTheme=$darkTheme")

            // Registrar imagen del pin en el nuevo estilo
            loadedStyle.addImage(PIN_IMAGE_ID, hangerPinBitmap(context, darkTheme))

            // Fuente GeoJSON vacía; se llena con setPinsSource
            loadedStyle.addSource(GeoJsonSource(PIN_SOURCE_ID))

            // Capa de símbolos que muestra los pines sobre el mapa
            loadedStyle.addLayer(
                SymbolLayer(PIN_LAYER_ID, PIN_SOURCE_ID).withProperties(
                    iconImage(PIN_IMAGE_ID),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(true),
                    iconAnchor(Property.ICON_ANCHOR_CENTER),
                    iconSize(1f)
                )
            )

            // Activar ubicación del usuario si el permiso fue concedido
            if (hasLocationPermission(context)) {
                @Suppress("MissingPermission") // El guard hasLocationPermission ya verificó el permiso
                map.locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(context, loadedStyle).build()
                )
                @Suppress("MissingPermission")
                map.locationComponent.isLocationComponentEnabled = true
                // NONE: la cámara no sigue al usuario automáticamente.
                map.locationComponent.cameraMode = CameraMode.NONE
            }

            // Restaurar pines sobre el nuevo estilo sin mover la cámara
            setPinsSource(loadedStyle, currentPins)
            style = loadedStyle
        }
    }

    // Box para superponer el Boton de "ir a mi ubicación" sobre el mapa
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                stableMapView.apply {
                    // MapView requiere onCreate antes de getMapAsync.
                    onCreate(null)

                    postDelayed({
                        Log.d("ReviewsMap", "DIMENSIONES: ${width}x${height} | ATTACHED: $isAttachedToWindow")
                    }, 2000)

                    getMapAsync { map ->
                        // Mantener atribución visible (requerido por OpenFreeMap).
                        map.uiSettings.isAttributionEnabled = true

                        // Detectar toque sobre un pin y propagar su reviewId
                        map.addOnMapClickListener { latLng ->
                            val screenPoint = map.projection.toScreenLocation(latLng)
                            val feature = map
                                .queryRenderedFeatures(screenPoint, PIN_LAYER_ID)
                                .firstOrNull()
                            val reviewId = feature?.getStringProperty(REVIEW_ID_PROPERTY)
                            if (reviewId != null) {
                                currentOnPinClick(reviewId)
                                true
                            } else {
                                false
                            }
                        }

                        // Asignar el mapa dispara LaunchedEffect y carga el estilo inicial.
                        mapLibreMap = map

                        // Centrar en Bogotá si no hay pines al abrir.
                        if (currentPins.isEmpty()) {
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(DEFAULT_CENTER, DEFAULT_ZOOM)
                            )
                        }
                    }
                }
            },
            update = { view ->
                // Solo actuar si la lista de pines cambió desde el último render.
                if (pins != renderedPins) {
                    view.getMapAsync { map ->
                        style?.let { loadedStyle ->
                            setPinsSource(loadedStyle, pins) // actualizar GeoJSON
                            fitCamera(map, pins)             // reencuadrar cámara
                            renderedPins = pins
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Solo mostrar el botón si el permiso de ubicación está concedido.
        if (hasLocationPermission(context)) {
            FloatingActionButton(
                onClick = {
                    // Pedir la ubicación actual y centrar la cámara sobre ella.
                    coroutineScope.launch {
                        val locationSource = FusedLocationDataSourceImpl(context)
                        val coords = locationSource.getCurrentLocation()
                        if (coords != null) {
                            mapLibreMap?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(coords.latitude, coords.longitude),
                                    MY_LOCATION_ZOOM
                                ),
                                CAMERA_ANIMATION_MS
                            )
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = stringResource(R.string.cd_my_location)
                )
            }
        }
    }

    // Delegar el ciclo de vida del MapView al LifecycleOwner del composable.
    DisposableEffect(lifecycleOwner, stableMapView) {
        var started = false
        var resumed = false
        var destroyed = false

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> if (!started && !destroyed) {
                    stableMapView.onStart()
                    started = true
                }
                Lifecycle.Event.ON_RESUME -> if (!resumed && !destroyed) {
                    stableMapView.onResume()
                    resumed = true
                }
                Lifecycle.Event.ON_PAUSE -> if (resumed && !destroyed) {
                    stableMapView.onPause()
                    resumed = false
                }
                Lifecycle.Event.ON_STOP -> if (started && !destroyed) {
                    stableMapView.onStop()
                    started = false
                }
                Lifecycle.Event.ON_DESTROY -> if (!destroyed) {
                    stableMapView.onDestroy()
                    destroyed = true
                    resumed = false
                    started = false
                }
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // Limpiar estados del MapView al salir de la composición.
            if (resumed && !destroyed) stableMapView.onPause()
            if (started && !destroyed) stableMapView.onStop()
            if (!destroyed) stableMapView.onDestroy()
        }
    }
}

// Verifica que al menos uno de los dos permisos de ubicación esté concedido.
private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

// Escribe el GeoJSON con los pines actuales sin tocar la cámara.
// Se llama al cargar un estilo nuevo y cuando cambia la lista de pines.
private fun setPinsSource(style: Style, pins: List<ReviewMapPin>) {
    val source = style.getSourceAs<GeoJsonSource>(PIN_SOURCE_ID) ?: return
    val features = pins.map { pin ->
        Feature
            .fromGeometry(Point.fromLngLat(pin.coordinates.longitude, pin.coordinates.latitude))
            .apply { addStringProperty(REVIEW_ID_PROPERTY, pin.reviewId) }
    }
    source.setGeoJson(FeatureCollection.fromFeatures(features))
}

// Encuadra la cámara según cuántos pines hay.
// Se llama solo cuando cambia la lista; nunca por cambio de tema.
private fun fitCamera(map: MapLibreMap, pins: List<ReviewMapPin>) {
    // Deduplicar coordenadas para evitar un LatLngBounds degenerado.
    val cameraPins = pins.distinctBy { it.coordinates }

    when (cameraPins.size) {
        0 -> return // sin pines, no mover la cámara
        1 -> {
            val pin = cameraPins.first()
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(pin.coordinates.latitude, pin.coordinates.longitude),
                    SINGLE_PIN_ZOOM
                ),
                CAMERA_ANIMATION_MS
            )
        }
        else -> {
            // Construir bounds que engloba todos los pines
            val bounds = LatLngBounds.Builder()
            cameraPins.forEach { pin ->
                bounds.include(LatLng(pin.coordinates.latitude, pin.coordinates.longitude))
            }
            map.animateCamera(
                CameraUpdateFactory.newLatLngBounds(bounds.build(), BOUNDS_PADDING_PX),
                CAMERA_ANIMATION_MS
            )
        }
    }
}

// Convierte el drawable del pin a Bitmap 96x96px para agregarlo en el estilo de MapLibre.
private fun hangerPinBitmap(context: Context, darkTheme: Boolean): Bitmap {
    val drawable = ContextCompat.getDrawable(context, pinDrawableRes(darkTheme))
        ?: return createBitmap(1, 1) // fallback: bitmap mínimo si el drawable no existe
    return drawable.toBitmap(width = 96, height = 96)
}
