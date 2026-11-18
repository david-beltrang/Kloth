# Feature: Mapa de reviews + ubicación

Documentación de la funcionalidad que permite **publicar una reseña con la ubicación del usuario** y **ver las reseñas de las últimas 24 horas en un mapa interactivo**.

El documento explica qué hace cada archivo, qué hace cada función relevante, cómo se comunican las capas entre sí y cómo se habla con Firestore, Google Location y OpenFreeMap/MapLibre.

---

## 1. Resumen funcional

La feature tiene dos mitades que se complementan:

1. **Captura de ubicación al crear una reseña** (`createReview`)
   - Al entrar a la pantalla se pide permiso de ubicación.
   - El usuario puede activar/desactivar un switch "Incluir mi ubicación".
   - Si está activo, se obtiene la ubicación, se **redondea a 3 decimales (~110 m)** y se guarda `latitude`/`longitude` junto a la reseña en Firestore.
   - Si el usuario lo desactiva o no hay ubicación disponible, la reseña se publica igual (sin coordenadas).

2. **Mapa de reseñas recientes** (`reviewsMap`)
   - Se abre desde el botón de mapa en **Explorar**.
   - Consulta Firestore por las reseñas creadas en las **últimas 24 horas** (máximo 200).
   - Une cada reseña con su autor (`users`) y pinta un pin por reseña que tenga coordenadas.
   - Al tocar un pin se abre un bottom sheet con el resumen; desde ahí se navega a la reseña completa.
   - El mapa y los pines cambian entre modo claro/oscuro siguiendo el tema de la app.

---

## 2. Arquitectura general

El proyecto sigue una arquitectura por capas con **MVVM + Hilt (DI)**:

```
┌───────────────────────────── UI · Jetpack Compose ─────────────────────────────┐
│  ExploreScreenContent ──botón mapa──► AppNavigation ──► ReviewsMapScreen        │
│  CreateReviewScreenContent ──switch ubicación──► CreateReviewScreen             │
└───────────────────────────────────────┬────────────────────────────────────────┘
                                        │ StateFlow + callbacks
                                        ▼
                     ViewModels (@HiltViewModel)
                     ├─ ReviewsMapViewModel
                     └─ CreateReviewViewModel
                                        │
                                        ▼
                     Repositories (@Singleton)
                     ├─ ReviewRepository
                     ├─ LocationRepository
                     └─ UserRepository
                                        │
                     ┌──────────────────┴───────────────────┐
                     ▼                                      ▼
     ReviewFirestoreDataSourceImpl              FusedLocationDataSourceImpl
     (Firebase Firestore)                       (Google FusedLocationProvider)
                     │                                      │
                     ▼                                      ▼
             Firestore "reviews"                    GPS / última ubicación
             Firestore "users"

     ReviewsMap (Compose + AndroidView) ──► MapLibre ──► OpenFreeMap (HTTPS)
```

### Regla de dependencias

- La **UI** no conoce Firebase, MapLibre ni Android Location: solo habla con ViewModels y modelos de dominio (`ReviewMapPin`, `Coordinates`).
- Los **ViewModels** no conocen implementaciones: usan repositorios inyectados.
- Los **repositorios** orquestan: consultan data sources, unen datos y mapean DTO → modelo de UI.
- Los **data sources** son la única capa que conoce SDKs externos (Firestore, FusedLocation).
- **MapLibre** queda aislado en un solo archivo (`ReviewsMap.kt`); el resto del código trabaja con `ReviewMapPin`.

---

## 3. Dependencias y configuración

### 3.1 Dependencias agregadas

En `project/gradle/libs.versions.toml`:

| Versión | Valor |
|---|---|
| `maplibre` | `11.8.3` |
| `playServicesLocation` | `21.3.0` |

Librerías:

```toml
maplibre-android        = { module = "org.maplibre.gl:android-sdk", version.ref = "maplibre" }
play-services-location  = { module = "com.google.android.gms:play-services-location", version.ref = "playServicesLocation" }
```

En `project/app/build.gradle.kts`:

```kotlin
// MapLibre
implementation(libs.maplibre.android)
// Google Location
implementation(libs.play.services.location)
```

- **MapLibre** renderiza el mapa (fork open source de Mapbox GL).
- **play-services-location** provee `FusedLocationProviderClient`, que combina GPS, Wi-Fi y red para obtener la ubicación con bajo consumo.

Configuración Android relevante:

- `minSdk = 26`, `targetSdk = 37`, `compileSdk = 37`.
- `buildConfig = true` (necesario para `BuildConfig.DEBUG`).

### 3.2 Inicialización de MapLibre

`BaseApplication.kt`:

```kotlin
FirebaseApp.initializeApp(this)
MapLibre.getInstance(this)
```

`MapLibre.getInstance(context)` **debe** ejecutarse antes de crear cualquier `MapView`. Por eso vive en la `Application` y no en la pantalla.

> Nota: en este mismo archivo se eliminó la lógica que conectaba Firestore/Auth al emulador. Ahora la app usa **Firebase real**; el emulador solo aplica al backend Express vía Retrofit (`AppModule`) y a los emuladores de Firebase que se levanten manualmente.

---

## 4. Flujo A — Crear una reseña con ubicación

### 4.1 Archivos nuevos

| Archivo | Rol |
|---|---|
| `data/local/ReviewMapPin.kt` | Define `Coordinates` y `ReviewMapPin` (modelos de dominio ligeros, sin Android ni MapLibre). |
| `data/datasource/services/LocationDataSource.kt` | Contrato para obtener la ubicación actual. |
| `data/datasource/implementation/location/FusedLocationDataSourceImpl.kt` | Implementación con Google Fused Location Provider. |
| `data/injection/LocationDataSourceModule.kt` | Módulo Hilt que une el contrato con la implementación. |
| `data/repository/LocationRepository.kt` | Obtiene y **redondea** la ubicación antes de entregarla. |

### 4.2 Archivos modificados

| Archivo | Cambio |
|---|---|
| `CreateReviewState.kt` | Nuevos campos `includeLocation` y `locationPermissionDenied`. |
| `createReviewScreen.kt` | Launcher de permisos, solicitud al entrar y manejo del switch. |
| `CreateReviewScreenContent.kt` | UI del switch de ubicación y mensaje de error. |
| `CreateReviewViewModel.kt` | Captura de coordenadas al publicar y callbacks de permiso. |
| `ReviewRepository.kt` | `createReview(...)` acepta `coordinates` y las guarda en el DTO. |
| `ReviewDto.kt` | `CreateReviewDto` agrega `latitude`/`longitude`. |

### 4.3 Función por función

#### `Coordinates` y `ReviewMapPin` — `data/local/ReviewMapPin.kt`

```kotlin
data class Coordinates(val latitude: Double, val longitude: Double)
```

Modelo mínimo de coordenadas. Se usa tanto en la captura como en el mapa.

```kotlin
data class ReviewMapPin(
    val reviewId: String,
    val articleId: String,
    val authorId: String,
    val authorName: String,
    val avatarUrl: String?,
    val rating: Float,
    val reviewText: String,
    val timeAgo: String,
    val coordinates: Coordinates
)
```

Es el modelo que consume la UI del mapa. Evita filtrar tipos de Firestore o MapLibre hacia Compose. `articleId` y `authorId` se guardan por completitud, aunque hoy la UI solo usa `reviewId` para navegar.

#### `LocationDataSource` — `data/datasource/services/LocationDataSource.kt`

```kotlin
interface LocationDataSource {
    suspend fun getCurrentLocation(): Coordinates?
}
```

Contrato simple: devuelve coordenadas o `null`. Permite cambiar la implementación (por ejemplo, un fake en tests) sin tocar el repositorio.

#### `FusedLocationDataSourceImpl` — `.../location/FusedLocationDataSourceImpl.kt`

`getCurrentLocation(): Coordinates?`

Estrategia en dos pasos:

1. **`client.lastLocation.await()`** — lectura instantánea de la última ubicación conocida. Si existe, se devuelve de inmediato (sin esperar GPS).
2. Si no hay última ubicación, **`getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)`** dentro de `withTimeoutOrNull(10_000)` — espera hasta 10 s una lectura nueva.

Manejo de errores:

- `CancellationException` se relanza (no se debe tragar la cancelación de corrutinas).
- Cualquier otra excepción se loguea y se devuelve `null`.

Anotación `@SuppressLint("MissingPermission")`: la verificación real del permiso ocurre en la UI (`MainActivity`/`CreateReviewScreen`), no aquí.

#### `LocationDataSourceModule` — `data/injection/LocationDataSourceModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class LocationDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindLocationDataSource(
        implementation: FusedLocationDataSourceImpl
    ): LocationDataSource
}
```

Le dice a Hilt que cuando alguien pida `LocationDataSource`, entregue `FusedLocationDataSourceImpl` como singleton.

#### `LocationRepository` — `data/repository/LocationRepository.kt`

`getCurrentCoordinates(): Result<Coordinates>`

- Llama al data source.
- Si no hay ubicación → `Result.failure`.
- Si hay → redondea latitud y longitud a **3 decimales**:

```kotlin
val rounded = Coordinates(
    latitude  = (raw.latitude  * SCALE).roundToInt() / SCALE,
    longitude = (raw.longitude * SCALE).roundToInt() / SCALE
)
```

`SCALE = 10^3 = 1000`. Redondear a 3 decimales reduce la precisión a ~110 m, suficiente para el mapa y mucho más protector de la privacidad que la ubicación exacta.

#### `CreateReviewState` — `ui/screens/createReview/CreateReviewState.kt`

Nuevos campos:

```kotlin
val includeLocation: Boolean = false,
val locationPermissionDenied: Boolean = false
```

- `includeLocation`: estado del switch.
- `locationPermissionDenied`: muestra el error cuando el usuario pidió ubicación pero no se pudo obtener.

#### `CreateReviewScreen` — `ui/screens/createReview/createReviewScreen.kt`

Es el orquestador de permisos de la pantalla.

- **`locationPermissionLauncher`**: `rememberLauncherForActivityResult(RequestMultiplePermissions())`. Pide `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION` juntos. Considera concedido si **cualquiera** de los dos fue aceptado (`fine == true || coarse == true`).
- **`LaunchedEffect(Unit)`**: al entrar a la pantalla:
  - Si ya hay permiso → `viewModel.onIncludeLocationChanged(true)` (el switch arranca activo).
  - Si no → lanza el launcher.
- **`onIncludeLocationChanged`**: al tocar el switch:
  - Si se activa y ya hay permiso → actualiza el ViewModel.
  - Si se activa sin permiso → lanza el launcher.
  - Si se desactiva → actualiza el ViewModel directamente.
- El resto de `LaunchedEffect` ya existentes cargan el producto y navegan al terminar el envío.

#### `CreateReviewScreenContent` — `ui/screens/createReview/CreateReviewScreenContent.kt`

Agrega al final del formulario:

- Un divisor.
- Una fila con icono `LocationOn`, textos `create_review_location_label` / `create_review_location_hint` y un `Switch` con `testTag("includeLocationSwitch")`.
- Si `uiState.locationPermissionDenied` es `true`, muestra `create_review_location_required_error` en color de error.

El nuevo parámetro `onIncludeLocationChanged: (Boolean) -> Unit = {}` mantiene compatibles los previews existentes.

#### `CreateReviewViewModel` — `ui/screens/createReview/CreateReviewViewModel.kt`

Nuevas funciones:

- **`onIncludeLocationChanged(enabled)`**: actualiza `includeLocation` y limpia `locationPermissionDenied`. Solo debe llamarse cuando el permiso ya está resuelto.
- **`onLocationPermissionResult(granted)`**:
  - Concedido → `includeLocation = true`.
  - Denegado → `includeLocation = false`, `locationPermissionDenied = true`.
- **`submitReview()`** (modificado):
  1. Si `includeLocation` es `true`, llama `locationRepository.getCurrentCoordinates()`.
  2. Si no se obtienen coordenadas → detiene el envío y marca `locationPermissionDenied = true`.
  3. Si hay coordenadas (o el switch está apagado) → llama `reviewRepository.createReview(..., coordinates = coordinates)`.

#### `ReviewRepository.createReview(...)` — `data/repository/ReviewRepository.kt`

Nuevo parámetro opcional `coordinates: Coordinates? = null`. Se traduce al DTO:

```kotlin
val dto = CreateReviewDto(
    userId = userId,
    articleId = articleId,
    rating = rating,
    text = text,
    createdAt = Instant.now().toString(),
    latitude = coordinates?.latitude,
    longitude = coordinates?.longitude
)
reviewDataSource.createReview(dto)
```

Si no hay coordenadas, los campos quedan `null` y la reseña no aparecerá en el mapa.

### 4.4 Secuencia completa

```
Usuario abre CreateReviewScreen
   │
   ├─ LaunchedEffect: ¿permiso? ── no ──► launcher (FINE + COARSE)
   │                                └─────► onLocationPermissionResult(granted)
   │
   ├─ Usuario activa switch ──► (si falta permiso) launcher
   │
   └─ Usuario pulsa "Publicar"
         │
         ├─ includeLocation = true
         │     └─ LocationRepository.getCurrentCoordinates()
         │           └─ FusedLocationDataSourceImpl
         │                 ├─ lastLocation
         │                 └─ getCurrentLocation (10 s)
         │           └─ redondeo a 3 decimales
         │
         ├─ ¿coordenadas null? ── sí ──► error en UI, no se publica
         │
         └─ ReviewRepository.createReview(coordinates)
               └─ CreateReviewDto(latitude, longitude)
                     └─ Firestore: reviews.add(dto)
```

---

## 5. Flujo B — Mapa de reviews

### 5.1 Archivos nuevos

| Archivo | Rol |
|---|---|
| `ui/screens/reviewsMap/ReviewsMapScreen.kt` | Composable de entrada; conecta ViewModel y UI. |
| `ui/screens/reviewsMap/ReviewsMapScreenContent.kt` | Layout: top bar, estados (cargando/error/vacío/mapa) y bottom sheet. |
| `ui/screens/reviewsMap/ReviewsMapState.kt` | Estado de UI (`pins`, `selectedPin`, `isLoading`, `errorMessage`). |
| `ui/screens/reviewsMap/ReviewsMapViewModel.kt` | Carga las reseñas y gestiona selección de pin. |
| `ui/screens/reviewsMap/components/ReviewsMap.kt` | Integración real con MapLibre (MapView, estilos, pines, cámara, ciclo de vida). |
| `ui/screens/reviewsMap/components/ReviewPinSheet.kt` | Contenido del bottom sheet de un pin. |
| `ui/screens/reviewsMap/ReviewsMapScreenPreview.kt` | Previews de Compose (claro, oscuro y vacío) sin instanciar MapView. |
| `res/drawable/ic_hanger_pin_dark.xml` | Pin para fondo oscuro: círculo blanco con el gancho delineado en negro. |
| `res/drawable/ic_hanger_pin_light.xml` | Pin para fondo claro: círculo negro con el gancho delineado en blanco. |

### 5.2 Archivos modificados

| Archivo | Cambio |
|---|---|
| `data/dto/ReviewDto.kt` | Coordenadas y `createdAt` en la clase base; mapeo a `ReviewMapPin`; filtro temporal. |
| `data/datasource/implementation/firestore/ReviewFirestoreDataSourceImpl.kt` | Nueva consulta `getReviewsCreatedSince`. |
| `data/datasource/services/ReviewRemoteDataSource.kt` | Se declara `getReviewsCreatedSince`. |
| `data/repository/ReviewRepository.kt` | `getRecentReviewPins`. |
| `data/datasource/services/ReviewRetrofitService.kt` | Stub `getReviewsSince` (no usado por el mapa). |
| `data/datasource/implementation/retrofit/ReviewRetrofitDataSourceImpl.kt` | Implementación del stub (no usada por el mapa). |
| `ui/navigation/AppRoutes.kt` | Ruta `ReviewsMap("reviews_map")`. |
| `ui/navigation/AppNavigation.kt` | Composable de la ruta y navegación desde Explorar. |
| `ui/navigation/NavigationLogic.kt` | `reviews_map` oculta top bar y bottom bar. |
| `ui/screens/explore/ExploreScreen.kt` | Nuevo callback `onMapClick`. |
| `ui/screens/explore/ExploreScreenContent.kt` | Botón de mapa junto a la barra de búsqueda. |
| `res/values/strings.xml` | Textos del mapa y de la ubicación. |
| `AndroidManifest.xml` | Permisos de ubicación y de red local. |

### 5.3 Función por función

#### `ReviewDtoGeneric` — `data/dto/ReviewDto.kt`

La clase base ahora declara:

```kotlin
abstract val createdAt: String?
abstract val latitude: Double?
abstract val longitude: Double?
```

Y dos funciones nuevas:

- **`toReviewMapPin(author: UserDtoGeneric?): ReviewMapPin?`**
  Devuelve `null` si falta `latitude` o `longitude` (la ubicación es opcional). Si existen, construye el pin con:
  - `reviewId`, `articleId`, `authorId`
  - `authorName` (o `"Usuario eliminado"` si no se encontró el autor)
  - `avatarUrl`, `rating`, `reviewText`
  - `timeAgo` calculado con `timeAgo(createdAt)`
  - `coordinates = Coordinates(lat, lng)`

- **`isCreatedAtOrAfter(since: Instant): Boolean`**
  Filtro defensivo: parsea `createdAt` como ISO-8601 y comprueba que no sea anterior a `since`. Si el parseo falla o `createdAt` es `null`, devuelve `false` y la reseña se excluye del mapa.

`ReviewRetrofitDto` y `ReviewFirestoreDto` implementan ambas y agregan los campos `latitude`/`longitude` (por defecto `null`).

#### `ReviewFirestoreDataSourceImpl.getReviewsCreatedSince(...)`

```kotlin
private const val MAP_REVIEWS_LIMIT = 200L

override suspend fun getReviewsCreatedSince(sinceIso: String): List<ReviewDtoGeneric> {
    val snapshot = db.collection("reviews")
        .whereGreaterThanOrEqualTo("createdAt", sinceIso)
        .orderBy("createdAt", Query.Direction.DESCENDING)
        .limit(MAP_REVIEWS_LIMIT)
        .get().await()
    return snapshot.documents.mapNotNull { doc ->
        doc.toObject(ReviewFirestoreDto::class.java)?.copy(reviewId = doc.id)
    }
}
```

- Consulta **solo las reseñas con `createdAt >= since`**.
- Ordena de más reciente a más antigua.
- Limita a **200** documentos para no saturar el mapa ni la facturación.
- `copy(reviewId = doc.id)` inyecta el ID real del documento (no viene dentro del documento).

> `createdAt` se guarda como **String ISO-8601 UTC** (`Instant.now().toString()`), por eso la comparación de rango funciona lexicográficamente. Si algún documento usara otro formato/zona horaria, el filtro `isCreatedAtOrAfter` lo descarta de forma defensiva.

#### `ReviewRepository.getRecentReviewPins(...)`

```kotlin
suspend fun getRecentReviewPins(now: Instant = Instant.now()): Result<List<ReviewMapPin>> = try {
    val since = now.minus(24, ChronoUnit.HOURS)
    val reviews = reviewDataSource.getReviewsCreatedSince(since.toString())
    val usersById = userRepository.getAllUsers().associateBy { it.toUserProfileInfo().id }
    val pins = reviews
        .filter { it.eliminated != true && it.isCreatedAtOrAfter(since) }
        .mapNotNull { it.toReviewMapPin(usersById[it.authorIdKey()]) }
    Result.success(pins)
} catch (e: Exception) {
    Log.e("ReviewRepository", "Error al cargar los reviews del mapa", e)
    Result.failure(e)
}
```

Pasos:

1. Calcula `since = ahora - 24 h`.
2. Consulta Firestore.
3. Carga **todos los usuarios** una sola vez y los indexa por ID (evita N consultas por reseña).
4. Filtra reseñas eliminadas y las que no cumplen el rango temporal.
5. Mapea a `ReviewMapPin`; las reseñas sin coordenadas se descartan con `mapNotNull`.
6. Devuelve `Result` para que el ViewModel distinga éxito/error.

#### `ReviewsMapState` — `ui/screens/reviewsMap/ReviewsMapState.kt`

```kotlin
data class ReviewsMapState(
    val pins: List<ReviewMapPin> = emptyList(),
    val selectedPin: ReviewMapPin? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
```

- `pins`: lo que se pinta en el mapa.
- `selectedPin`: si no es `null`, se muestra el bottom sheet.
- `isLoading` / `errorMessage`: estados de la pantalla.

#### `ReviewsMapViewModel`

- **`loadRecentReviews()`**
  - Marca `isLoading = true` y limpia el error.
  - Cancela la carga anterior (`loadJob?.cancel()`) para evitar resultados viejos si el usuario refresca varias veces.
  - Llama `reviewRepository.getRecentReviewPins()`.
  - `ensureActive()`: si la corrutina fue cancelada, no toca el estado.
  - `fold`: éxito → actualiza `pins`; error → guarda `errorMessage`.
- **`onPinSelected(reviewId)`**: busca el pin por ID y lo pone en `selectedPin`.
- **`onPinDismissed()`**: limpia `selectedPin` (cierra el sheet).

#### `ReviewsMapScreen`

- `LaunchedEffect(Unit)` → `loadRecentReviews()` al entrar (carga única; el refresco es manual).
- Conecta callbacks:
  - `onBackClick` → volver.
  - `onRetryClick` / `onRefreshClick` → recargar.
  - `onPinSelected` / `onPinDismissed` → sheet.
  - `onReviewClick(reviewId)` → navegar a la reseña.

#### `ReviewsMapScreenContent`

Estructura:

1. **Top bar** con botón atrás, título (`map_title`) y botón refrescar (`map_refresh`).
2. **Divisor**.
3. **Box central** con `when`:
   - `isLoading` → `CircularProgressIndicator`.
   - `errorMessage != null` → mensaje + botón `map_retry`.
   - `pins.isEmpty()` → texto `map_empty`.
   - Si hay pines → `mapContent(pins, onPinSelected)`.
4. **Bottom sheet** (`ModalBottomSheet`) cuando `selectedPin != null`, con `ReviewPinSheet`. Al abrir una reseña: cierra el sheet y llama `onReviewClick(pin.reviewId)`.

El parámetro `mapContent` es un **slot** con valor por defecto `ReviewsMap(...)`. Gracias a eso los previews pueden renderizar un placeholder sin crear un `MapView`.

#### `ReviewPinSheet`

- Reutiliza `ReviewHeader` (autor, tiempo, avatar, rating).
- Muestra el texto de la reseña (máx. 6 líneas, con elipsis).
- Botón "Ver reseña" (`review_pin_open`).
- Aplica `navigationBarsPadding()` para no quedar tapado por la barra del sistema.

#### `ReviewsMap` — integración con MapLibre

Es el único archivo que conoce MapLibre. Constantes principales:

```kotlin
MAP_STYLE_LIGHT_URL = "https://tiles.openfreemap.org/styles/positron"
MAP_STYLE_DARK_URL  = "https://tiles.openfreemap.org/styles/dark"

PIN_SOURCE_ID      = "review_pins_source"
PIN_LAYER_ID       = "review_pins_layer"
PIN_IMAGE_ID       = "review_pin_hanger"
REVIEW_ID_PROPERTY = "reviewId"

CAMERA_ANIMATION_MS = 600
BOUNDS_PADDING_PX   = 140
SINGLE_PIN_ZOOM     = 14.0
DEFAULT_ZOOM        = 11.0
DEFAULT_CENTER      = LatLng(4.7110, -74.0721) // Bogotá
```

Funciones internas:

- **`mapStyleUrl(darkTheme)`**: elige `positron` (claro) o `dark` (oscuro).
- **`pinDrawableRes(darkTheme)`**: elige `ic_hanger_pin_light` o `ic_hanger_pin_dark`.
- **`hasLocationPermission(context)`**: `true` si FINE o COARSE está concedido.
- **`setPinsSource(style, pins)`**: construye un `FeatureCollection` GeoJSON con un `Point` por pin y una propiedad `reviewId`; lo escribe en la fuente.
- **`fitCamera(map, pins)`**:
  - 0 pines → no mueve la cámara.
  - 1 pin → zoom 14 sobre ese pin.
  - Varios → `LatLngBounds` que los engloba, con padding de 140 px.
  - Deduplica coordenadas para evitar bounds degenerados.
- **`hangerPinBitmap(context, darkTheme)`**: convierte el vector del pin a bitmap de 96×96 para registrarlo en el estilo.

Comportamiento del composable:

- `remember(context) { MapView(context) }` → un solo `MapView` por contexto.
- `rememberUpdatedState` para `pins` y `onPinClick`, y así evitar valores obsoletos en lambdas asíncronas.
- **`LaunchedEffect(mapLibreMap, darkTheme)`**:
  1. Pone `style = null` para bloquear actualizaciones mientras carga el nuevo estilo.
  2. `map.setStyle(...)` con el estilo del tema.
  3. Al cargar: registra la imagen del pin, crea la fuente GeoJSON y la capa `SymbolLayer` (`iconImage`, `iconAllowOverlap`, `iconIgnorePlacement`, `iconAnchor = CENTER`, `iconSize = 1`).
  4. Si hay permiso, activa `locationComponent` con `CameraMode.NONE` (muestra el punto azul pero no sigue al usuario).
  5. Repinta los pines existentes (`setPinsSource`) sin mover la cámara.
- **`AndroidView`**:
  - `factory`: `onCreate(null)`, `getMapAsync`, activa atribución (requerida por OpenFreeMap), registra el click listener y centra en Bogotá si no hay pines.
  - `update`: si la lista de pines cambió desde el último render (`renderedPins`), actualiza el GeoJSON y reencuadra la cámara. `renderedPins` evita reencuadrar en cada recomposición.
- **Click en pin**: convierte el `LatLng` tocado a coordenadas de pantalla y usa `queryRenderedFeatures(screenPoint, PIN_LAYER_ID)`. Si hay feature, lee `reviewId` y llama `onPinClick`.
- **`DisposableEffect`**: reenvía `onStart/onResume/onPause/onStop/onDestroy` al `MapView` y limpia en `onDispose`. Esto es obligatorio con MapLibre para liberar recursos.

#### `ReviewsMapScreenPreview`

Tres previews:

- Claro (`KlothTheme(darkTheme = false)`).
- Oscuro (`uiMode = UI_MODE_NIGHT_YES`).
- Vacío.

Todos usan `mapContent = { _, _ -> MapPlaceholder() }` para no instanciar MapLibre en el editor.

---

## 6. Integración en navegación y UI

### 6.1 Ruta

`AppRoutes.kt`:

```kotlin
object ReviewsMap : AppRoutes("reviews_map")
```

### 6.2 Navegación

`AppNavigation.kt`:

```kotlin
composable(AppRoutes.Explore.route) {
    ExploreScreen(
        ...,
        onMapClick = { navController.navigate(AppRoutes.ReviewsMap.route) }
    )
}

composable(AppRoutes.ReviewsMap.route) {
    ReviewsMapScreen(
        onBackClick = { navController.popBackStack() },
        onReviewClick = { reviewId ->
            navController.navigate(AppRoutes.Review.createRoute(reviewId))
        }
    )
}
```

Flujo de navegación:

```
Feed / BottomBar → Explore → [botón mapa] → ReviewsMap
                                              │
                                              └─ pin → [Ver reseña] → ReviewScreen
```

### 6.3 Visibilidad de barras

`NavigationLogic.kt` agrega `ReviewsMap` a:

- `noTopBarScreens`
- `noBottomBarScreens`

Así el mapa ocupa toda la pantalla y usa su propia top bar interna.

### 6.4 Botón en Explorar

- `ExploreScreen.kt`: nuevo callback `onMapClick: () -> Unit = {}` que se propaga a `ExploreScreenContent`.
- `ExploreScreenContent.kt`: la barra de búsqueda pasa a un `Row` con:
  - `SearchBar` con `weight(1f)`.
  - `FilledTonalIconButton` con icono `Icons.Outlined.Map`, `contentDescription = cd_map` y `testTag("exploreMapButton")`.

---

## 7. Comunicación con la API / backend

### 7.1 Firestore (backend real de la feature)

La app habla con **Cloud Firestore** usando el SDK de Firebase.

**Colección:** `reviews`

**Documento (campos relevantes para el mapa):**

```json
{
  "userId": "uid_del_autor",
  "articleId": "id_del_articulo",
  "rating": 5,
  "text": "Excelente calidad...",
  "createdAt": "2026-11-11T14:32:10.123Z",
  "eliminated": false,
  "latitude": 4.711,
  "longitude": -74.072,
  "likesCount": 0,
  "commentsCount": 0
}
```

**Operaciones usadas por la feature:**

| Operación | Método | Uso |
|---|---|---|
| Crear | `db.collection("reviews").add(dto)` | Guardar reseña con/sin coordenadas. |
| Consultar rango | `whereGreaterThanOrEqualTo("createdAt", since).orderBy("createdAt", DESC).limit(200)` | Pins de las últimas 24 h. |
| Leer usuarios | `userDataSource.getAllUsers()` | Unir autor con reseña. |

**Autenticación y reglas:**

`firestore.rules` permite lectura/escritura a cualquier usuario autenticado:

```javascript
match /{document=**} {
  allow read, write: if request.auth != null;
}
```

Implicaciones:

- El mapa solo funciona con sesión iniciada.
- Cualquier usuario autenticado puede leer `latitude`/`longitude` de las reseñas (mitigado por el redondeo a ~110 m).

**Índices:** la consulta usa rango + orden sobre el **mismo campo** (`createdAt`), por lo que Firestore la cubre con el índice de un solo campo; no requiere índice compuesto.

### 7.2 Retrofit / backend Express

El proyecto conserva una capa Retrofit para un backend Express (`AppModule`, base `http://10.0.2.2:3000/` en emulador, `http://127.0.0.1:3000/` en dispositivo).

Para el mapa se agregó un stub simétrico:

```kotlin
@GET("/reviews")
suspend fun getReviewsSince(@Query("since") since: String): List<ReviewRetrofitDto>
```

y su implementación:

```kotlin
override suspend fun getReviewsCreatedSince(sinceIso: String): List<ReviewDtoGeneric> =
    service.getReviewsSince(sinceIso)
```

**Importante:** `ReviewRepository` inyecta directamente `ReviewFirestoreDataSourceImpl` (la implementación concreta de Firestore), no la interfaz `ReviewRemoteDataSource`. Por eso:

- La ruta Retrofit **no se usa** en el mapa hoy.
- `ReviewRemoteDataSource`, `ReviewRetrofitDataSourceImpl` y `ReviewRetrofitService` existen pero no están cableados al flujo real.
- El comentario del código lo deja explícito: *"No conectado: el backend Express no está en el repo"*.

Si algún día se migra al backend Express, bastaría cambiar la inyección de `ReviewRepository` para usar la interfaz y asegurar que el endpoint `/reviews?since=...` devuelva `latitude`/`longitude`.

### 7.3 OpenFreeMap + MapLibre

- **MapLibre** descarga estilos y tiles por HTTPS desde **OpenFreeMap**:
  - Claro: `https://tiles.openfreemap.org/styles/positron`
  - Oscuro: `https://tiles.openfreemap.org/styles/dark`
- Requiere conexión a internet; no hay caché offline configurada.
- La atribución del mapa está activada (`uiSettings.isAttributionEnabled = true`), condición de uso de OpenFreeMap.
- Los pines **no** son tiles: son una capa `SymbolLayer` alimentada por un `GeoJsonSource` local. Cada pin es un `Feature` con `Point(lng, lat)` y la propiedad `reviewId`.

### 7.4 Google Location

- `LocationServices.getFusedLocationProviderClient(context)`.
- Primero `lastLocation` (rápido), luego `getCurrentLocation(HIGH_ACCURACY)` con timeout de 10 s.
- No hay actualizaciones continuas ni ubicación en segundo plano: solo una lectura puntual al publicar.

### 7.5 Emuladores

`project/firebase.json` define emuladores de Auth (9099), Firestore (8080) y UI (4000). `project/.firebaserc` fija el proyecto `kloth-7e866`.

- `BaseApplication` ya **no** redirige Firestore/Auth al emulador; usa Firebase real.
- `MainActivity` pide `ACCESS_LOCAL_NETWORK` en Android 17+ (API 37) y solo en debug, para que el emulador pueda hablar con servicios en `10.0.2.2`.
- `network_security_config.xml` permite tráfico HTTP sin TLS (necesario para el backend Express local).

---

## 8. Permisos

Declarados en `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_LOCAL_NETWORK" />
```

Flujo de permisos de ubicación:

1. **`MainActivity`** (al iniciar la app):
   - Si ya hay FINE o COARSE → pasa directo a pedir red local.
   - Si no → lanza `RequestMultiplePermissions(FINE, COARSE)`.
   - Al terminar, pide `ACCESS_LOCAL_NETWORK` si aplica (API 37 + debug).
2. **`CreateReviewScreen`** (al entrar):
   - Repite la comprobación y, si hace falta, lanza el launcher.
   - Al conceder, activa el switch automáticamente.
3. **`ReviewsMap`**: no pide permisos; solo activa el punto azul de ubicación si ya están concedidos.

No se solicita `ACCESS_BACKGROUND_LOCATION`.

---

## 9. Tema claro / oscuro

- `ReviewsMap` recibe `darkTheme: Boolean = isSystemInDarkTheme()`.
- Es la **misma fuente** que usa `KlothTheme`, por lo que el mapa sigue el tema de la app.
- Al cambiar el tema:
  1. `LaunchedEffect(mapLibreMap, darkTheme)` se vuelve a ejecutar.
  2. Se pone `style = null`.
  3. `setStyle` carga el estilo nuevo (positron/dark).
  4. Se vuelven a registrar imagen, fuente y capa (el estilo anterior se descarta).
  5. Se repintan los pines sin mover la cámara.
- El pin cambia de drawable según el tema para mantener contraste.

---

## 10. Modelo de datos y decisiones

### 10.1 Ventana de 24 horas

`getRecentReviewPins` calcula `since = now - 24 h`. El mapa muestra solo actividad reciente, lo que evita consultas enormes y mantiene el mapa relevante.

### 10.2 Límite de 200

`MAP_REVIEWS_LIMIT = 200` acota la consulta y el número de pines. No hay paginación; si se necesita más, habría que añadir scroll/carga incremental o clustering.

### 10.3 Ubicación opcional

- Sin coordenadas → la reseña se guarda y se ve en el feed, pero **no** aparece en el mapa.
- `toReviewMapPin` devuelve `null` en ese caso y `mapNotNull` la descarta.

### 10.4 Privacidad

- Redondeo a **3 decimales (~110 m)**.
- Solo una lectura puntual, sin seguimiento.
- El mapa no muestra el punto exacto del usuario, solo el punto azul del componente de ubicación (si hay permiso).

### 10.5 Filtros defensivos

- `eliminated != true`: no se muestran reseñas borradas.
- `isCreatedAtOrAfter(since)`: si `createdAt` falta o no parsea, se excluye.
- `mapNotNull`: si falta latitud o longitud, se excluye.

### 10.6 Aislamiento de MapLibre

Todo el código de MapLibre vive en `ReviewsMap.kt`. El resto de la app usa `ReviewMapPin` y `Coordinates`. Esto permite:

- Previsualizar la pantalla sin `MapView`.
- Cambiar de motor de mapas tocando un solo archivo.
- Testear ViewModel/Repository sin Android.

---

## 11. Estado actual, notas y pendientes

Cosas que conviene saber al tocar esta feature:

| Tema | Detalle |
|---|---|
| **Capa Retrofit muerta** | `ReviewRemoteDataSource`, `ReviewRetrofitDataSourceImpl` y `ReviewRetrofitService` no están inyectados; el flujo real usa Firestore. |
| **String sin uso** | `create_review_location_denied` está definido pero no se usa (la UI usa `create_review_location_required_error`). |
| **Campos sin uso** | `ReviewMapPin.articleId` y `ReviewMapPin.authorId` se rellenan pero la UI no los lee. |
| **Logs de debug** | `BaseApplication` y `ReviewsMap` tienen `Log.d` de diagnóstico (incluido un `postDelayed` de 2 s que imprime dimensiones). |
| **Sin tests del mapa** | No hay tests unitarios/instrumentados para `ReviewsMap`, `LocationRepository` ni `getRecentReviewPins`. |
| **Dependencia de red** | El mapa necesita internet para estilos/tiles. |
| **Formato de `createdAt`** | Debe ser ISO-8601 UTC consistente; el filtro defensivo descarta lo que no parsee. |
| **`google-services.json`** | Está en `.gitignore`; sin él el proyecto no compila. Debe obtenerse del proyecto Firebase. |
| **`.firebaserc`** | Necesario para `firebase emulators:start`; conviene versionarlo. |

---

## 12. Cómo probar manualmente

1. Colocar `google-services.json` en `project/app/`.
2. Compilar y ejecutar en emulador o dispositivo (`minSdk 26`).
3. Aceptar el permiso de ubicación cuando se solicite.
4. Ir a un artículo → **Crear reseña**:
   - Verificar que el switch "Incluir mi ubicación" aparece y arranca activo si hay permiso.
   - Publicar con el switch activo.
   - En Firestore, comprobar que el documento de `reviews` tiene `latitude`/`longitude` con 3 decimales.
5. Ir a **Explorar** → botón de mapa (icono `Map`).
6. Verificar:
   - Loading mientras carga.
   - Pines de las reseñas de las últimas 24 h con coordenadas.
   - Estado vacío si no hay reseñas.
   - Botón de refrescar y de reintentar.
7. Tocar un pin → bottom sheet con autor, rating, texto y botón "Ver reseña".
8. Pulsar "Ver reseña" → navega a `ReviewScreen`.
9. Cambiar el tema del sistema (claro/oscuro) con el mapa abierto → el estilo y los pines deben actualizarse sin salir de la pantalla.

### Emuladores (opcional)

```bash
cd project
firebase emulators:start
```

Esto levanta Auth/Firestore/UI según `firebase.json`. Recuerda que la app, tal como está hoy, usa **Firebase real** para Firestore/Auth.
