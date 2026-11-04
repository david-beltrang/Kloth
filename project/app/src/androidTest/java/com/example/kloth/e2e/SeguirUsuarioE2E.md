# Guía de `SeguirUsuarioE2E`

Este documento explica la prueba `SeguirUsuarioE2E`: qué verifica, cómo prepara Firebase, cómo usa los tags de Compose y cómo se conecta con las pantallas y los ViewModels de Kloth.

## Qué verifica

La prueba recorre el flujo de una persona que inicia sesión, abre el perfil de otra, revisa sus datos, la sigue y comprueba que su publicación aparezca en la pestaña **Siguiendo**.

El recorrido es:

1. Arrancar la app sin una sesión abierta y llegar al login.
2. Iniciar sesión con la cuenta de prueba de Ana.
3. Encontrar la publicación de Carlos en el feed.
4. Abrir el perfil de Carlos desde el avatar de esa publicación.
5. Comprobar los datos del perfil y que todavía no se le sigue.
6. Pulsar **Seguir** y comprobar que el botón y el contador cambian.
7. Volver a Inicio, abrir **Siguiendo** y encontrar la publicación de Carlos.

La prueba usa la interfaz real. Solo prepara las cuentas y documentos que necesita; la relación de seguimiento se crea al tocar el botón en pantalla.

## Archivos relacionados

| Archivo | Función en el flujo |
| --- | --- |
| `SeguirUsuarioE2E.kt` | Datos de prueba, preparación/limpieza y acciones/assertions del test. |
| `HiltTestRunner.kt` | Inicia la instrumentación con `HiltTestApplication`, necesaria para la inyección de dependencias de Android. |
| `app/build.gradle.kts` | Selecciona `HiltTestRunner` y declara las dependencias instrumentadas. |
| `gradle/libs.versions.toml` | Centraliza las versiones de AndroidX Test y Espresso usadas por la prueba. |
| `ui/navigation/AppNavigation.kt` | Decide qué pantalla se muestra y conecta los eventos de navegación. |
| `ui/screens/login/LoginScreenContent.kt` | Expone tags para localizar el login y sus campos. |
| `ui/screens/feed/FeedScreen.kt` | Presenta el feed y dispara su carga. |
| `ui/screens/feed/FeedViewModel.kt` | Carga las publicaciones y filtra las de las personas seguidas. |
| `ui/screens/feed/components/FeedTabRow.kt` | Muestra y marca las pestañas **Siguiendo** y **Para ti**. |
| `ui/screens/feed/components/PostCard.kt` | Dibuja una publicación; el avatar abre el perfil de su autor. |
| `ui/screens/profile/ProfileScreen.kt` | Recibe el id del perfil y recoge el estado del `ProfileViewModel`. |
| `ui/screens/profile/ProfileViewModel.kt` | Carga datos y estado de seguimiento; ejecuta el seguimiento solicitado por la UI. |
| `ui/screens/profile/ProfileScreenContent.kt` | Compone el encabezado etiquetado del perfil. |
| `ui/screens/profile/components/ProfileHeaderSection.kt` | Expone los datos, contador y botón **Seguir** que comprueba el test. |
| `data/repository/UserRepository.kt` | Capa intermedia entre los ViewModels y el acceso a usuarios/seguimientos. |
| `data/datasource/implementation/firestore/UserFirestoreDataSourceImpl.kt` | Lee y cambia los documentos de seguimiento en Firestore. |
| `data/repository/ArticleRepository.kt` | Reúne publicaciones, autores y reseñas y los convierte para el feed. |
| `data/datasource/implementation/firestore/ArticleFirestoreDataSourceImpl.kt` | Lee la colección `articles` de Firestore. |

## Datos de la prueba

Los datos se agrupan en dos `data class` sencillos:

- `UsuarioDePrueba`: correo, contraseña, username, nombre completo, biografía, ubicación y sitio web.
- `PublicacionDePrueba`: id, título y descripción.

Las instancias de prueba representan a Ana (quien inicia sesión) y Carlos (a quien se sigue). La publicación tiene el id `e2e_follow_article` y se crea con `createdAt = Instant.now()`. El repositorio ordena el feed por fecha descendente, de modo que esta publicación reciente se encuentra al principio.

Los documentos relevantes en Firestore son:

```text
users/{idDeAna}
users/{idDeCarlos}
articles/e2e_follow_article
users/{idDeAna}/following/{idDeCarlos}
users/{idDeCarlos}/followers/{idDeAna}
```

Los dos últimos documentos **no se crean durante la preparación**. La aplicación los crea como resultado del clic en **Seguir**. Firestore representa la relación desde ambos lados: a quién sigue Ana y quién sigue a Carlos.

## Ciclo de preparación y reglas JUnit

En la clase, el orden de las reglas es importante:

```kotlin
@get:Rule(order = 0) val preparacion = PreparacionDeLaPrueba()
@get:Rule(order = 1) val hiltRule = HiltAndroidRule(this)
@get:Rule(order = 2) val composeRule = createAndroidComposeRule<MainActivity>()
```

La regla de orden `0` es la más externa. Su `before()` configura los permisos y los emuladores, crea o inicia las cuentas y escribe los documentos **antes de que `MainActivity` se abra**. Eso importa porque la actividad inicia en el splash y puede consultar la sesión y Firebase inmediatamente.

Después, `HiltAndroidRule` prepara la inyección y `createAndroidComposeRule` lanza `MainActivity`. El único método `@Before` de la clase hace `hiltRule.inject()`; la preparación Firebase está en la regla externa, no en ese método.

### Qué hace `before()`

1. Concede `POST_NOTIFICATIONS` en Android 13 o posterior y `ACCESS_LOCAL_NETWORK` en API 37 o posterior. Se hace antes de abrir la actividad para que los diálogos de permiso no interfieran con los clics.
2. Inicializa Firebase y dirige Auth y Firestore a los emuladores. `10.0.2.2` es la dirección del host vista desde el emulador Android; Auth usa el puerto `9099` y Firestore `8080`.
3. Cierra cualquier sesión que hubiera quedado abierta.
4. Crea la cuenta de Auth de Ana o inicia sesión si ya existía. Hace lo mismo con Carlos y conserva ambos UID.
5. Elimina únicamente los dos documentos de relación entre Ana y Carlos. Esto deja el estado inicial correcto incluso si una corrida anterior se interrumpió antes de su limpieza.
6. Guarda los documentos de usuario con los contadores `followersCount = 0` y `followingCount = 0`, y guarda la publicación.
7. Vuelve a cerrar sesión. Así el splash debe navegar al login.

Las operaciones suspendidas se ejecutan dentro de `runBlocking` porque `ExternalResource.before()` es síncrono. Cada operación Firebase termina con `await()` antes de continuar, así que la actividad no se abre con los datos a medio guardar.

`crearOIniciarCuenta()` permite repetir el test: crea la cuenta si no existe y la reutiliza si Auth responde que el correo ya está registrado.

### Qué hace `after()`

Al terminar el caso, borra la publicación, los dos documentos de relación conocidos, los documentos de Ana y Carlos, y las cuentas de Auth. El borrado está limitado a los ids del test; no vacía colecciones completas.

La limpieza previa de la relación es necesaria además de `after()`: el proceso puede cerrarse a la fuerza y no alcanzar a ejecutar la limpieza final.

## Qué significa la semántica de Compose

Compose expone un árbol de **semántica** junto a lo que dibuja en pantalla. Cada nodo semántico describe un elemento y propiedades que las herramientas de accesibilidad y de pruebas pueden leer, como texto, selección, acciones o `testTag`.

Por ejemplo:

```kotlin
composeRule.waitUntil(timeoutMillis = 5000) {
    composeRule.onAllNodesWithTag("profileUsername")
        .fetchSemanticsNodes()
        .any { nodo ->
            nodo.config.getOrNull(SemanticsProperties.Text)
                ?.any { texto -> texto.text == usuarioSeguido.nombreUsuario } == true
        }
}
```

La condición se evalúa repetidamente hasta que sea verdadera o pasen 5000 ms:

1. `onAllNodesWithTag("profileUsername")` busca los elementos actuales con ese tag. Se usa la forma plural porque durante la carga puede que todavía no haya ningún elemento; una búsqueda singular fallaría inmediatamente.
2. `fetchSemanticsNodes()` obtiene la lista de nodos que Compose tiene en ese momento. No es una espera: solo consulta el estado actual.
3. `.any { ... }` pregunta si alguno de esos nodos ya contiene el valor esperado.
4. `nodo.config` contiene las propiedades semánticas del nodo.
5. `getOrNull(SemanticsProperties.Text)` pide su texto. Devuelve `null` si ese nodo todavía no publicó texto; el acceso seguro `?.` convierte ese caso en “aún no coincide”, en vez de provocar un error.
6. La propiedad `Text` puede contener varios fragmentos (`AnnotatedString`); el segundo `.any` compara el contenido de cada fragmento con el username esperado.
7. `== true` hace que la condición sea claramente falsa cuando no hay propiedad o no coincide el texto.

El tag `profileUsername` ya puede existir mientras el perfil muestra valores iniciales o provisionales. Por eso no basta con esperar a que aparezca el tag: se espera a que **el contenido del nodo sea el username de Carlos**. Una vez cumplida esa condición, las assertions verifican el resto de los campos.

`waitUntil` evita pausas fijas como `Thread.sleep`: el test continúa tan pronto como la condición se cumple y falla con timeout si no ocurre. Los 5 segundos son el límite para cada transición/carga, no una pausa que siempre se consume completa.

En esta prueba no se usa `useUnmergedTree`. Los tags de texto, los contadores y el botón se encuentran en los nodos que Compose expone normalmente; los elementos clicables agrupan las propiedades de sus hijos en el nodo accesible. Por ejemplo, `followButton` contiene el texto del botón y `profileFollowersCount` incluye el valor que se comprueba.

## Por qué hay varias esperas

La app realiza navegación y lecturas asíncronas. Cada espera cubre un paso distinto:

| Espera | Qué evita | Por qué la comprobación es suficiente |
| --- | --- | --- |
| `loginScreen` | Que el test interactúe antes de que el splash termine su decisión. | El tag solo aparece al llegar al contenido de login; después se verifica con `assertIsDisplayed()`. |
| `homeScreen` tras login | Que el test continúe mientras Auth y la navegación todavía procesan el inicio de sesión. | Confirma que la ruta del feed ya está en Compose. |
| `post_{id}` | Que se intente tocar el avatar antes de que Firestore cargue y dibuje la publicación. | El id del tag identifica la publicación concreta sembrada por esta prueba. |
| `profileScreen` | Que el test continúe antes de que termine la navegación desde el avatar. | Confirma que se dibujó el contenido de perfil; no asegura aún que sus datos hayan llegado. |
| `profileUsername` con texto esperado | Que se validen datos iniciales/provisionales del perfil. | Inspecciona `SemanticsProperties.Text` y espera el username de Carlos. |
| `followButton` con texto “Dejar de Seguir” | Que se lea el estado anterior al terminar la transacción de seguimiento. | El tag permanece igual; cambia el texto cuando el ViewModel recibe el resultado. |
| `homeScreen` tras volver | Que el test toque una pestaña antes de volver a la ruta Inicio. | Confirma la pantalla de feed después de la navegación. |
| `tabFollowing` seleccionado | Que el post compartido con “Para ti” dé un falso positivo. | Comprueba la propiedad semántica `Selected`, no solo que la pestaña esté presente. |
| `post_{id}` tras elegir “Siguiendo” | Que el filtrado asíncrono termine antes de buscar la publicación. | Confirma que el feed filtrado contiene el post esperado. |

## Flujo de UI y conexión con los ViewModels

### 1. Splash y login

`AppNavigation` empieza en `AppRoutes.Splash`. `SplashViewModel` espera su breve demora y revisa `AuthRepository.currentUser`. La regla de preparación dejó la sesión cerrada, así que el destino es `AppRoutes.Login`.

`LoginScreenContent` publica los tags `loginScreen`, `loginEmailField`, `loginPasswordField` y `btnLogin`. El test escribe las credenciales en los `KlothTextField` y pulsa el botón. El evento llega a `LoginViewModel.onLoginClick()`, que llama a `AuthRepository.signIn`. Si Auth tiene éxito, el estado de navegación cambia y `AppNavigation` abre `AppRoutes.Feed`.

### 2. Carga del feed y publicación

Al entrar a `FeedScreen`, su `LaunchedEffect` llama a `FeedViewModel.getAllPosts()`. El ViewModel solicita las publicaciones a `ArticleRepository.getFeedPosts()` y los ids seguidos a `UserRepository`.

`ArticleRepository` combina artículos, usuarios y reseñas y crea objetos `PostItem`, incluyendo el id y username del autor. `FeedScreenContent` dibuja cada `PostItem` con `PostCard`.

`PostCard` usa `post_{id}` en la tarjeta y `postAvatar_{id}` en el avatar. La tarjeta completa abre el detalle del producto; el clic concreto del avatar invoca `onProfileClick(post.creatorId)`. `AppNavigation` convierte ese UID en la ruta `AppRoutes.UserProfile`.

### 3. Perfil y datos del usuario

`ProfileScreen` recibe `profileUserId` y llama a `ProfileViewModel.refreshProfile(profileUserId)`. El ViewModel carga el usuario mediante `UserRepository.getUserById()` y consulta por separado si la cuenta actual ya sigue a Carlos.

La respuesta actualiza `ProfileState`; `ProfileScreen` pasa esos valores a `ProfileScreenContent`, que coloca `ProfileHeaderSection` en la pantalla. El contenido tiene el tag `profileScreen`; el encabezado expone `profileUsername`, `profileEmail`, `profileBio`, `profileLocation`, `profileWebsite` y `profileFollowersCount`.

Como la consulta del perfil y la consulta de seguimiento son asíncronas, se espera el username antes de comparar los campos y el estado del botón. La preparación garantiza que, para estas dos cuentas, la relación anterior fue borrada y los contadores se escribieron en cero.

### 4. Seguir a Carlos

`followButton` está en el `Box` clicable del encabezado. El clic llama a `ProfileScreen.onFollowClick`, que ejecuta `ProfileViewModel.toggleFollow()`.

La cadena de datos es:

```text
clic en ProfileHeaderSection
  -> ProfileScreen
  -> ProfileViewModel.toggleFollow()
  -> UserRepository.followOrUnfollowUser()
  -> UserFirestoreDataSourceImpl.followOrUnfollowUser()
  -> transacción de Firestore
```

La transacción crea `users/{idDeAna}/following/{idDeCarlos}` y `users/{idDeCarlos}/followers/{idDeAna}`, e incrementa `followingCount` y `followersCount`. El ViewModel recibe el resultado y actualiza `isFollowing` y `followersCount`; el encabezado cambia el botón a **Dejar de Seguir**. La prueba espera ese texto y después comprueba el contador `1`.

### 5. Feed “Siguiendo”

El test vuelve a Inicio usando la descripción accesible de `R.string.nav_home`, que publica `KlothBottomNavigation`. Al regresar, `FeedScreen` vuelve a cargar sus datos.

`FeedTabRow` tiene el tag `tabFollowing`. Al tocarlo, llama a `FeedViewModel.onTabSelected(0)`, que vuelve a consultar los ids de seguimiento. Para el índice `0`, `filterPosts` conserva solo publicaciones cuyo `creatorId` pertenece a esos ids. El test primero espera que la pestaña tenga `SemanticsProperties.Selected == true`, y luego que aparezca `post_e2e_follow_article`.

La assertion final se hace sobre la tarjeta y comprueba que contiene el username de Carlos. Así se valida que no solo hay un post cualquiera, sino el que escribió el usuario seguido.

## Tags que usa la prueba

Los tags son nombres de prueba añadidos con `Modifier.testTag`. Son identificadores estables para el test; no cambian el texto ni la navegación de la app.

| Tag | Componente | Uso |
| --- | --- | --- |
| `loginScreen` | `LoginScreenContent` | Esperar y comprobar login. |
| `loginEmailField` | `KlothTextField` dentro del login | Introducir el correo. |
| `loginPasswordField` | `KlothTextField` dentro del login | Introducir la contraseña. |
| `btnLogin` | Botón principal del login | Ejecutar autenticación. |
| `homeScreen` | `FeedScreen` | Comprobar llegada o retorno al feed. |
| `tabFollowing` | `FeedTabRow` | Abrir y comprobar la pestaña Siguiendo. |
| `post_{id}` | Tarjeta de `PostCard` | Encontrar la publicación preparada. |
| `postAvatar_{id}` | Avatar de autor en `PostCard` | Abrir perfil del autor. |
| `profileScreen` | `ProfileScreenContent` | Comprobar la pantalla de perfil. |
| `profileUsername`, `profileEmail`, `profileBio` | `ProfileHeaderSection` | Comparar datos del perfil. |
| `profileLocation`, `profileWebsite` | `ProfileHeaderSection` | Comparar ubicación y sitio web. |
| `profileFollowersCount` | Columna clicable de estadísticas | Comprobar los contadores `0` y `1`. |
| `followButton` | Caja clicable del botón | Pulsar y leer el texto del estado. |

## Ejecutar la prueba

La prueba necesita Auth y Firestore locales, y un emulador/dispositivo Android disponible. Desde el terminal del proyecto:

```powershell
firebase emulators:start --only auth,firestore --project kloth-7e866
```

En otra terminal, ejecuta solo esta clase:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.example.kloth.e2e.SeguirUsuarioE2E" --console=plain
```

También se puede abrir `SeguirUsuarioE2E.kt` en Android Studio y ejecutar la clase. Si se acaba de cambiar una dependencia, primero hay que sincronizar Gradle y reconstruir el APK de test.

`mockk-android` no debe volver a añadirse a `androidTest`: incluía `libmockkjvmtiagent.so`, con segmentos nativos no alineados a 16 KB, y provocaba el aviso del sistema durante la ejecución. La dependencia MockK de los tests locales (`testImplementation`) es independiente.
