package com.example.kloth.e2e

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.example.kloth.MainActivity
import com.example.kloth.R
import com.example.kloth.data.dto.ArticleFirestoreDto
import com.example.kloth.data.dto.UserFirestoreDto
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.time.Instant
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource

private const val ETIQUETA_LOG = "SeguirUsuarioE2E"
private const val TIEMPO_LIMPIEZA_OPERACION_MS = 3_000L
private const val TIEMPO_LIMPIEZA_TOTAL_MS = 20_000L

private data class UsuarioDePrueba(
    val correo: String,
    val contrasena: String,
    val nombreUsuario: String,
    val nombreCompleto: String,
    val biografia: String,
    val ubicacion: String = "",
    val sitioWeb: String = ""
)

private data class PublicacionDePrueba(
    val id: String,
    val titulo: String,
    val descripcion: String
)

private val usuarioQueIniciaSesion = UsuarioDePrueba(
    correo = "ana.e2e@kloth.test",
    contrasena = "kloth123456",
    nombreUsuario = "ana_kloth",
    nombreCompleto = "Ana Torres",
    biografia = "Cuenta de prueba para seguir usuarios"
)

private val usuarioSeguido = UsuarioDePrueba(
    correo = "carlos.e2e@kloth.test",
    contrasena = "kloth123456",
    nombreUsuario = "carlos_kloth",
    nombreCompleto = "Carlos Gomez",
    biografia = "Amante de la moda y las chaquetas",
    ubicacion = "Cali, Colombia",
    sitioWeb = "https://carloskloth.com"
)

private val publicacionDePrueba = PublicacionDePrueba(
    id = "e2e_follow_article",
    titulo = "Chaqueta azul de prueba",
    descripcion = "Publicacion de Carlos para la prueba E2E."
)

@HiltAndroidTest
class SeguirUsuarioE2E {

    // Este rule corre antes que Compose: deja listos permisos, emuladores y datos.
    @get:Rule(order = 0)
    val preparacion = PreparacionDeLaPrueba()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private var idUsuarioQueIniciaSesion = ""
    private var idUsuarioSeguido = ""

    @Before
    fun inyectarHilt() {
        hiltRule.inject()
    }

    @Test
    fun seguirAUnUsuarioYVerSuPublicacionEnSeguidos() {
        // El splash decide la ruta de forma asíncrona; esperamos a que aparezca el login.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("loginScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()

        // Login con la cuenta creada antes de abrir la app.
        composeRule.onNodeWithTag("loginEmailField").performTextInput(usuarioQueIniciaSesion.correo)
        composeRule.onNodeWithTag("loginPasswordField").performTextInput(usuarioQueIniciaSesion.contrasena)
        composeRule.onNodeWithTag("btnLogin").performClick()

        // La autenticación consulta Firebase y después navega; home no aparece al instante.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("homeScreen").assertIsDisplayed()

        // El feed hace otra consulta a Firestore después de abrirse.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("post_${publicacionDePrueba.id}")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("post_${publicacionDePrueba.id}").assertIsDisplayed()

        // El avatar navega usando el id del autor asociado a esta publicación.
        composeRule.onNodeWithTag("postAvatar_${publicacionDePrueba.id}").performClick()

        // Primero esperamos la pantalla de perfil; sus datos se cargan en una petición aparte.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("profileScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()

        // El perfil puede aparecer con datos provisionales antes de recibir la respuesta de Firestore.
        // Esperamos a que el nodo etiquetado contenga el nombre esperado, no solo a que exista.
        composeRule.waitUntil(timeoutMillis = 5000) {
            // Cada consulta obtiene los nodos que Compose muestra en ese momento.
            composeRule.onAllNodesWithTag("profileUsername")
                .fetchSemanticsNodes()
                .any { nodo ->
                    // Un SemanticsNode describe un elemento visible para accesibilidad y pruebas.
                    // getOrNull devuelve null si aún no tiene texto; en ese caso seguimos esperando.
                    nodo.config.getOrNull(SemanticsProperties.Text)
                        // Text es una lista porque un nodo puede publicar más de un fragmento de texto.
                        ?.any { texto -> texto.text == usuarioSeguido.nombreUsuario } == true
                }
        }

        composeRule.onNodeWithTag("profileUsername").assertTextEquals(usuarioSeguido.nombreUsuario)
        composeRule.onNodeWithTag("profileEmail").assertTextEquals(usuarioSeguido.correo)
        composeRule.onNodeWithTag("profileBio").assertTextEquals(usuarioSeguido.biografia)
        composeRule.onNodeWithTag("profileLocation").assertTextEquals(usuarioSeguido.ubicacion)
        composeRule.onNodeWithTag("profileWebsite").assertTextEquals(usuarioSeguido.sitioWeb)

        // Estos valores confirman que la preparación dejó la relación en estado inicial.
        composeRule.onNodeWithTag("profileFollowersCount").assertTextContains("0")
        composeRule.onNodeWithTag("followButton")
            .assertTextEquals(composeRule.activity.getString(R.string.profile_follow))

        // El seguimiento se crea desde el botón, igual que en el uso normal de la app.
        composeRule.onNodeWithTag("followButton").performClick()

        val textoDejarDeSeguir = composeRule.activity.getString(R.string.profile_unfollow)
        // El botón conserva su tag; esperamos que cambie su texto cuando termine la operación de Firebase.
        composeRule.waitUntil(timeoutMillis = 5000) {
            // El tag encuentra el botón aunque todavía conserve el texto anterior.
            composeRule.onAllNodesWithTag("followButton")
                .fetchSemanticsNodes()
                .any { nodo ->
                    // Se comprueba el texto publicado por el botón, no solo que siga en pantalla.
                    nodo.config.getOrNull(SemanticsProperties.Text)
                        ?.any { texto -> texto.text == textoDejarDeSeguir } == true
                }
        }
        composeRule.onNodeWithTag("profileFollowersCount").assertTextContains("1")
        composeRule.onNodeWithTag("followButton").assertTextEquals(textoDejarDeSeguir)

        // La barra inferior identifica Home con la descripción accesible localizada de la app.
        composeRule.onNodeWithContentDescription(
            composeRule.activity.getString(R.string.nav_home)
        ).performClick()

        // La navegación y la recarga del feed ocurren después del clic.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("homeScreen").assertIsDisplayed()

        // Abrir la pestaña "Siguiendo".
        composeRule.onNodeWithTag("tabFollowing").performClick()

        // La publicación también sale en "Para ti"; por eso primero comprobamos que cambió la pestaña.
        // Selected es una propiedad semántica que Material3 expone para la pestaña activa.
        composeRule.waitUntil(timeoutMillis = 5000) {
            // La pestaña existe antes y después del clic; comprobamos su estado, no su presencia.
            composeRule.onAllNodesWithTag("tabFollowing")
                .fetchSemanticsNodes()
                .any { nodo -> nodo.config.getOrNull(SemanticsProperties.Selected) == true }
        }
        composeRule.onNodeWithTag("tabFollowing").assertIsSelected()

        // Al seleccionar la pestaña, el ViewModel consulta los ids seguidos y filtra las publicaciones.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("post_${publicacionDePrueba.id}")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("post_${publicacionDePrueba.id}").assertIsDisplayed()
        // El nombre del autor también prueba que es la publicación correcta.
        composeRule.onNodeWithTag("post_${publicacionDePrueba.id}")
            .assertTextContains(usuarioSeguido.nombreUsuario)
    }

    inner class PreparacionDeLaPrueba : ExternalResource() {

        override fun before() {
            darPermisos()

            runBlocking {
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                FirebaseApp.initializeApp(context)

                // HiltTestApplication reemplaza a BaseApplication, por eso los
                // emuladores se configuran aquí.
                runCatching { Firebase.auth.useEmulator("10.0.2.2", 9099) }
                runCatching { Firebase.firestore.useEmulator("10.0.2.2", 8080) }

                Firebase.auth.signOut()

                idUsuarioQueIniciaSesion = crearOIniciarCuenta(usuarioQueIniciaSesion)
                Firebase.auth.signOut()

                idUsuarioSeguido = crearOIniciarCuenta(usuarioSeguido)
                Firebase.auth.signOut()

                // Elimina solo el vínculo entre estas cuentas, por si una corrida anterior se interrumpió.
                limpiarRelacionAnterior()
                // Se guardan los contadores en cero y una publicación conocida para hacer las comprobaciones.
                poblarUsuarioQueIniciaSesion()
                poblarUsuarioSeguido()
                poblarPublicacion()

                // La app debe arrancar sin sesión para que el splash vaya al login.
                Firebase.auth.signOut()
            }
        }

        override fun after() {
            runBlocking {
                val db = Firebase.firestore
                Firebase.auth.signOut()

                val limpiezaCompleta = withTimeoutOrNull(TIEMPO_LIMPIEZA_TOTAL_MS) {
                    // Borra solo los documentos y cuentas creados para este caso de prueba.
                    ejecutarLimpieza("publicación") {
                        db.collection("articles").document(publicacionDePrueba.id).delete().await()
                    }

                    if (idUsuarioQueIniciaSesion.isNotBlank() && idUsuarioSeguido.isNotBlank()) {
                        ejecutarLimpieza("relación de seguidos") {
                            db.collection("users").document(idUsuarioQueIniciaSesion)
                                .collection("following").document(idUsuarioSeguido).delete().await()
                        }
                        ejecutarLimpieza("relación de seguidores") {
                            db.collection("users").document(idUsuarioSeguido)
                                .collection("followers").document(idUsuarioQueIniciaSesion).delete().await()
                        }
                    }

                    if (idUsuarioQueIniciaSesion.isNotBlank()) {
                        ejecutarLimpieza("documento de quien inicia sesión") {
                            db.collection("users").document(idUsuarioQueIniciaSesion).delete().await()
                        }
                    }
                    if (idUsuarioSeguido.isNotBlank()) {
                        ejecutarLimpieza("documento de la persona seguida") {
                            db.collection("users").document(idUsuarioSeguido).delete().await()
                        }
                    }

                    borrarCuenta(usuarioQueIniciaSesion)
                    borrarCuenta(usuarioSeguido)
                    Firebase.auth.signOut()
                    true
                } ?: false

                if (!limpiezaCompleta) {
                    Log.w(ETIQUETA_LOG, "Se agotó el tiempo total de limpieza; termina la prueba de todos modos")
                }
            }
        }
    }

    // Una operación de limpieza lenta no debe dejar el test esperando indefinidamente.
    private suspend fun ejecutarLimpieza(nombre: String, accion: suspend () -> Unit) {
        try {
            val terminada = withTimeoutOrNull(TIEMPO_LIMPIEZA_OPERACION_MS) {
                accion()
                true
            } ?: false

            if (terminada) {
                Log.d(ETIQUETA_LOG, "Limpieza completada: $nombre")
            } else {
                Log.w(ETIQUETA_LOG, "Se agotó el tiempo al limpiar: $nombre")
            }
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Log.w(ETIQUETA_LOG, "No se pudo limpiar: $nombre", error)
        }
    }

    // Quita una relación que haya quedado de una ejecución interrumpida.
    private suspend fun limpiarRelacionAnterior() {
        val db = Firebase.firestore
        // Firestore guarda el vínculo en ambos sentidos: quién sigue y quién tiene seguidores.
        db.collection("users").document(idUsuarioQueIniciaSesion)
            .collection("following").document(idUsuarioSeguido).delete().await()
        db.collection("users").document(idUsuarioSeguido)
            .collection("followers").document(idUsuarioQueIniciaSesion).delete().await()
    }

    private suspend fun poblarUsuarioQueIniciaSesion() {
        guardarUsuario(idUsuarioQueIniciaSesion, usuarioQueIniciaSesion)
    }

    private suspend fun poblarUsuarioSeguido() {
        guardarUsuario(idUsuarioSeguido, usuarioSeguido)
    }

    private suspend fun guardarUsuario(id: String, usuario: UsuarioDePrueba) {
        val ahora = Instant.now().toString()
        Firebase.firestore.collection("users").document(id).set(
            UserFirestoreDto(
                userId = id,
                email = usuario.correo,
                password = usuario.contrasena,
                username = usuario.nombreUsuario,
                fullName = usuario.nombreCompleto,
                biography = usuario.biografia,
                location = usuario.ubicacion,
                website = usuario.sitioWeb,
                registeredAt = ahora,
                createdAt = ahora,
                updatedAt = ahora,
                followersCount = 0,
                followingCount = 0
            )
        ).await()
    }

    private suspend fun poblarPublicacion() {
        Firebase.firestore.collection("articles").document(publicacionDePrueba.id).set(
            ArticleFirestoreDto(
                articleId = publicacionDePrueba.id,
                typeArticle = "PRENDA",
                title = publicacionDePrueba.titulo,
                description = publicacionDePrueba.descripcion,
                createdAt = Instant.now().toString(),
                idUser = idUsuarioSeguido,
                eliminated = false,
                imageArticle = "",
                tags = emptyList()
            )
        ).await()
    }

    // Crea la cuenta en Auth o la reutiliza si ya existía de una corrida anterior.
    private suspend fun crearOIniciarCuenta(usuario: UsuarioDePrueba): String {
        return try {
            Firebase.auth.createUserWithEmailAndPassword(usuario.correo, usuario.contrasena).await()
            Firebase.auth.currentUser?.uid ?: error("No se obtuvo el uid de ${usuario.correo}")
        } catch (e: FirebaseAuthUserCollisionException) {
            Firebase.auth.signInWithEmailAndPassword(usuario.correo, usuario.contrasena).await()
            Firebase.auth.currentUser?.uid ?: error("No se obtuvo el uid de ${usuario.correo}")
        }
    }

    private suspend fun borrarCuenta(usuario: UsuarioDePrueba) {
        ejecutarLimpieza("cuenta ${usuario.correo}") {
            try {
                Firebase.auth.signInWithEmailAndPassword(usuario.correo, usuario.contrasena).await()
                Firebase.auth.currentUser?.delete()?.await()
            } finally {
                Firebase.auth.signOut()
            }
        }
    }

    private fun darPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            darPermiso(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= 37) {
            darPermiso(Manifest.permission.ACCESS_LOCAL_NETWORK)
        }
    }

    private fun darPermiso(permiso: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context: Context = instrumentation.targetContext
        if (context.checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED) {
            instrumentation.uiAutomation.grantRuntimePermission(context.packageName, permiso)
        }
    }
}
