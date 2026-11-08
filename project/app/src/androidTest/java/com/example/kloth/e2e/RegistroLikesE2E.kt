package com.example.kloth.e2e

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.kloth.MainActivity
import com.example.kloth.R
import com.example.kloth.data.dto.ArticleFirestoreDto
import com.example.kloth.data.dto.CommentFirestoreDto
import com.example.kloth.data.dto.ReviewFirestoreDto
import com.example.kloth.data.dto.UserFirestoreDto
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource

private const val ETIQUETA_LOG_REGISTRO = "RegistroLikesE2E"
private const val idAutorPrueba = "autor_registro_detalle_e2e"
private const val idArticuloPrueba = "articulo_registro_detalle_e2e"
private const val idResenaPrueba = "resena_registro_detalle_e2e"
private const val idComentarioPrueba = "comentario_registro_detalle_e2e"
private const val TIEMPO_LIMPIEZA_OPERACION_MS = 3_000L
private const val TIEMPO_LIMPIEZA_TOTAL_MS = 20_000L

private data class UsuarioRegistro(
    val correo: String,
    val contrasena: String,
    val nombreCompleto: String
)

private data class ArticuloPrueba(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val marca: String,
    val color: String,
    val categoria: String,
    val precio: Double
)

private data class ResenaPrueba(
    val id: String,
    val texto: String,
    val puntuacion: Int
)

private data class ComentarioPrueba(
    val id: String,
    val texto: String,
    val nombreAutor: String
)

private val articulo = ArticuloPrueba(
    id = idArticuloPrueba,
    titulo = "Chaqueta urbana E2E",
    descripcion = "Chaqueta ligera preparada para probar el detalle del artículo.",
    marca = "Kloth E2E",
    color = "Azul noche",
    categoria = "Chaquetas",
    precio = 129.5
)

private val resena = ResenaPrueba(
    id = idResenaPrueba,
    texto = "La chaqueta tiene buen acabado y es cómoda.",
    puntuacion = 5
)

private val comentario = ComentarioPrueba(
    id = idComentarioPrueba,
    texto = "Coincido, la calidad es muy buena.",
    nombreAutor = "autor_comentario_e2e"
)

@HiltAndroidTest
class RegistroLikesE2E {

    private val usuarioNuevo = UsuarioRegistro(
        correo = "registroAna@kloth.test",
        contrasena = "123456",
        nombreCompleto = "Ana Prueba E2E"
    )

    // La preparación ocurre antes de que Compose abra MainActivity.
    @get:Rule(order = 0)
    val preparacion = PreparacionPrueba()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun inyectarHilt() {
        hiltRule.inject()
    }

    @Test
    fun registroYLikeDeComentario() {
        // Sin una sesión guardada, el splash debe mostrar el login.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("loginScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()

        // Abre el formulario de registro desde el enlace del login.
        composeRule.onNodeWithTag("btnGoRegister").performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("registerScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("registerScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("registerNameField")
            .performTextInput(usuarioNuevo.nombreCompleto)
        composeRule.onNodeWithTag("registerEmailField")
            .performTextInput(usuarioNuevo.correo)
        composeRule.onNodeWithTag("registerPasswordField").performTextInput("1234")
        composeRule.onNodeWithTag("registerConfirmPasswordField").performTextInput("1234")
        composeRule.onNodeWithTag("btnRegister").performScrollTo().performClick()

        val mensajeContrasenaCorta = composeRule.activity.getString(R.string.auth_error_password_short)
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("registerErrorMessage")
                .fetchSemanticsNodes()
                .any { nodo ->
                    nodo.config.getOrNull(SemanticsProperties.Text)
                        ?.any { texto -> texto.text == mensajeContrasenaCorta } == true
                }
        }
        composeRule.onNodeWithTag("registerErrorMessage")
            .assertTextEquals(mensajeContrasenaCorta)

        // Corrige ambos campos y vuelve a enviar el formulario.
        composeRule.onNodeWithTag("registerPasswordField").performTextClearance()
        composeRule.onNodeWithTag("registerPasswordField")
            .performTextInput(usuarioNuevo.contrasena)
        composeRule.onNodeWithTag("registerConfirmPasswordField").performTextClearance()
        composeRule.onNodeWithTag("registerConfirmPasswordField")
            .performTextInput(usuarioNuevo.contrasena)
        composeRule.onNodeWithTag("btnRegister").performScrollTo().performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("homeScreen").assertIsDisplayed()

        // Abre el artículo preparado para que sea el primero del feed.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("post_$idArticuloPrueba")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("post_$idArticuloPrueba").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("detailScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("detailScreen").assertIsDisplayed()

        composeRule.onNodeWithTag("detailTitle").performScrollTo()
            .assertTextEquals(articulo.titulo)
        composeRule.onNodeWithTag("detailBrand").performScrollTo()
            .assertTextEquals(articulo.marca)
        composeRule.onNodeWithTag("detailCategoryTag").performScrollTo()
            .assertTextEquals("Prenda")
        composeRule.onNodeWithTag("detailPrice").performScrollTo()
            .assertTextContains("$", substring = true)
            .assertTextContains("129", substring = true)
        composeRule.onNodeWithTag("detailDescription").performScrollTo()
            .assertTextEquals(articulo.descripcion)
        composeRule.onNodeWithTag("detailColor").performScrollTo()
            .assertTextEquals(articulo.color)
        composeRule.onNodeWithTag("detailProductCategory").performScrollTo()
            .assertTextEquals(articulo.categoria)

        // La acción de comentarios de la review abre su pantalla con el comentario.
        val tagComentarios = "reviewCommentsButton_${resena.id}"
        composeRule.onNodeWithTag("detailScreen")
            .performScrollToNode(hasTestTag("reviewSection"))
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag(tagComentarios).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag(tagComentarios).performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("reviewScreen").assertIsDisplayed()

        // La pantalla primero puede mostrar estado vacío mientras carga Firestore.
        val tagTextoResena = "reviewText_${resena.id}"
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag(tagTextoResena)
                .fetchSemanticsNodes()
                .any { nodo ->
                    nodo.config.getOrNull(SemanticsProperties.Text)
                        ?.any { texto -> texto.text == resena.texto } == true
                }
        }

        val tagComentario = "comment_${comentario.id}"
        composeRule.onNodeWithTag("reviewList")
            .performScrollToNode(hasTestTag(tagComentario))
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag(tagComentario).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("commentText_${comentario.id}")
            .assertTextEquals(comentario.texto)

        val tagContadorLikes = "commentLikeCount_${comentario.id}"
        val tagBotonLike = "commentLikeButton_${comentario.id}"
        composeRule.onNodeWithTag(tagContadorLikes).assertTextEquals("0")
        composeRule.onNodeWithTag(tagBotonLike).performClick()

        // Espera a que termine la transacción que actualiza el contador en Firestore.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag(tagContadorLikes)
                .fetchSemanticsNodes()
                .any { nodo ->
                    nodo.config.getOrNull(SemanticsProperties.Text)
                        ?.any { texto -> texto.text == "1" } == true
                }
        }
        composeRule.onNodeWithTag(tagContadorLikes).assertTextEquals("1")

        // Vuelve al feed y abre de nuevo el mismo artículo y la misma review.
        composeRule.onNodeWithContentDescription(
            composeRule.activity.getString(R.string.cd_back)
        ).performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("detailScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithContentDescription(
            composeRule.activity.getString(R.string.cd_back)
        ).performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Home aparece antes de que FeedViewModel termine de cargar las publicaciones.
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("post_$idArticuloPrueba")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("post_$idArticuloPrueba").performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("detailScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("detailScreen")
            .performScrollToNode(hasTestTag("reviewSection"))
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag(tagComentarios).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag(tagComentarios).performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("reviewScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag(tagTextoResena)
                .fetchSemanticsNodes()
                .any { nodo ->
                    nodo.config.getOrNull(SemanticsProperties.Text)
                        ?.any { texto -> texto.text == resena.texto } == true
                }
        }
        composeRule.onNodeWithTag("reviewList")
            .performScrollToNode(hasTestTag(tagComentario))
        composeRule.onNodeWithTag(tagContadorLikes).assertTextEquals("1")
        composeRule.onNodeWithTag(tagBotonLike).performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag(tagContadorLikes)
                .fetchSemanticsNodes()
                .any { nodo ->
                    nodo.config.getOrNull(SemanticsProperties.Text)
                        ?.any { texto -> texto.text == "0" } == true
                }
        }
        composeRule.onNodeWithTag(tagContadorLikes).assertTextEquals("0")
    }

    inner class PreparacionPrueba : ExternalResource() {

        override fun before() {
            darPermisos()

            runBlocking {
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                FirebaseApp.initializeApp(context)
                runCatching { Firebase.auth.useEmulator("10.0.2.2", 9099) }
                runCatching { Firebase.firestore.useEmulator("10.0.2.2", 8080) }

                Firebase.auth.signOut()
                limpiarDatosAnteriores()
                poblarAutorResena()
                poblarArticulo()
                poblarResena()
                poblarComentario()
                Firebase.auth.signOut()
            }
        }

        override fun after() {
            runBlocking {
                val db = Firebase.firestore
                Firebase.auth.signOut()

                /*
                    Se tuvo que colocar un timeout porque el gradle build y otras funciones asincronas
                    no devolvían algun resultado, lo cual hacía que el test nunca terminara, ahora con ese timeout
                    el test termina e incluso se ejecutan cin exito los @before y @after
                 */

                val limpiezaCompleta = withTimeoutOrNull(TIEMPO_LIMPIEZA_TOTAL_MS) {
                    ejecutarLimpieza("likes y comentarios") { limpiarSubcoleccionesResena() }
                    ejecutarLimpieza("review") {
                        db.collection("reviews").document(resena.id).delete().await()
                    }
                    ejecutarLimpieza("artículo") {
                        db.collection("articles").document(articulo.id).delete().await()
                    }
                    ejecutarLimpieza("autor auxiliar") {
                        db.collection("users").document(idAutorPrueba).delete().await()
                    }
                    ejecutarLimpieza("perfil del usuario registrado") {
                        db.collection("users")
                            .whereEqualTo("email", usuarioNuevo.correo)
                            .get().await()
                            .documents.forEach { it.reference.delete().await() }
                    }
                    borrarCuentaNueva()
                    Firebase.auth.signOut()
                    true
                } ?: false

                if (!limpiezaCompleta) {
                    Log.w(ETIQUETA_LOG_REGISTRO, "Se agotó el tiempo total de limpieza")
                }
            }
        }
    }

    private suspend fun limpiarDatosAnteriores() {
        val db = Firebase.firestore
        limpiarSubcoleccionesResena()
        db.collection("reviews").document(resena.id).delete().await()
        db.collection("articles").document(articulo.id).delete().await()
        db.collection("users").document(idAutorPrueba).delete().await()
    }

    // Firestore no borra las subcolecciones cuando se elimina el documento padre.
    private suspend fun limpiarSubcoleccionesResena() {
        val referencia = Firebase.firestore.collection("reviews").document(resena.id)
        val comentarios = referencia.collection("comments").get().await().documents
        comentarios.forEach { comentario ->
            comentario.reference.collection("likes").get().await().documents
                .forEach { it.reference.delete().await() }
            comentario.reference.delete().await()
        }
        referencia.collection("likes").get().await().documents
            .forEach { it.reference.delete().await() }
    }

    private suspend fun poblarAutorResena() {
        val ahora = Instant.now().toString()
        Firebase.firestore.collection("users").document(idAutorPrueba).set(
            UserFirestoreDto(
                userId = idAutorPrueba,
                email = "autor.registro.detalle@kloth.test",
                username = "autor_detalle_e2e",
                fullName = "Autor de prueba",
                biography = "Autor para la publicación de prueba.",
                registeredAt = ahora,
                createdAt = ahora,
                updatedAt = ahora
            )
        ).await()
    }

    private suspend fun poblarArticulo() {
        Firebase.firestore.collection("articles").document(articulo.id).set(
            ArticleFirestoreDto(
                articleId = articulo.id,
                typeArticle = "PRENDA",
                title = articulo.titulo,
                description = articulo.descripcion,
                createdAt = Instant.now().plusSeconds(30).toString(),
                idUser = idAutorPrueba,
                eliminated = false,
                imageArticle = "",
                tags = listOf("e2e"),
                brand = articulo.marca,
                color = articulo.color,
                category = articulo.categoria,
                price = articulo.precio,
                size = "M",
                material = "Algodón"
            )
        ).await()
    }

    private suspend fun poblarResena() {
        Firebase.firestore.collection("reviews").document(resena.id).set(
            ReviewFirestoreDto(
                reviewId = resena.id,
                userId = idAutorPrueba,
                articleId = articulo.id,
                rating = resena.puntuacion,
                text = resena.texto,
                likesCount = 0,
                commentsCount = 1,
                createdAt = Instant.now().toString(),
                eliminated = false
            )
        ).await()
    }

    private suspend fun poblarComentario() {
        Firebase.firestore.collection("reviews").document(resena.id)
            .collection("comments").document(comentario.id).set(
                CommentFirestoreDto(
                    commentId = comentario.id,
                    userId = idAutorPrueba,
                    username = comentario.nombreAutor,
                    text = comentario.texto,
                    createdAt = Instant.now().toString(),
                    likesCount = 0
                )
            ).await()
    }

    private suspend fun borrarCuentaNueva() {
        ejecutarLimpieza("cuenta ${usuarioNuevo.correo}") {
            try {
                Firebase.auth.signInWithEmailAndPassword(
                    usuarioNuevo.correo,
                    usuarioNuevo.contrasena
                ).await()
                Firebase.auth.currentUser?.delete()?.await()
            } finally {
                Firebase.auth.signOut()
            }
        }
    }

    private suspend fun ejecutarLimpieza(nombre: String, accion: suspend () -> Unit) {
        try {
            val terminada = withTimeoutOrNull(TIEMPO_LIMPIEZA_OPERACION_MS) {
                accion()
                true
            } ?: false
            if (terminada) {
                Log.d(ETIQUETA_LOG_REGISTRO, "Limpieza completada: $nombre")
            } else {
                Log.w(ETIQUETA_LOG_REGISTRO, "Tiempo agotado al limpiar: $nombre")
            }
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Log.w(ETIQUETA_LOG_REGISTRO, "No se pudo limpiar: $nombre", error)
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
