package com.example.kloth.dataSourceTest

import android.util.Log
import com.example.kloth.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImpl
import com.example.kloth.data.dto.CreateCommentDto
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewFirestoreDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ReviewFirestoreDataSourceTest {

    private val logTag = "ReviewFirestoreTest"
    // Los marcadores de Logcat permiten ubicar el await que no termina.
    private val testArticleId = "article_review_datasource_test"
    private val db = Firebase.firestore
    private lateinit var datasource: ReviewFirestoreDataSourceImpl

    @Before
    fun setup() = runTest {
        Log.d(logTag, "setup: configurando emulador")
        grantLocalNetworkPermissionForFirebaseEmulator()
        datasource = ReviewFirestoreDataSourceImpl(db)
        // Limpia los datos propios de estos casos sin recorrer toda la coleccion.
        Log.d(logTag, "setup: iniciando limpieza")
        limpiarDatos()
        Log.d(logTag, "setup: limpieza terminada")
        Unit
    }

    @After
    fun tearDown() = runTest {
        // Elimina las resenas, comentarios y likes creados por la prueba.
        Log.d(logTag, "tearDown: iniciando limpieza")
        limpiarDatos()
        Log.d(logTag, "tearDown: limpieza terminada")
        Unit
    }

    private suspend fun limpiarDatos() {
        Log.d(logTag, "limpiarDatos: consultando reseñas de prueba")
        // Limpia solo las reseñas identificadas como datos de estas pruebas.
        val createdReviews = db.collection("reviews")
            .whereEqualTo("articleId", testArticleId)
            .get()
            .await()
        Log.d(logTag, "limpiarDatos: ${createdReviews.size()} reseñas coincidentes")
        for (review in createdReviews) datasource.deleteReview(review.id)

        Log.d(logTag, "limpiarDatos: borrando documentos fijos")
        datasource.deleteReview("review_comment_test")
        datasource.deleteReview("review_like_test")
        Log.d(logTag, "limpiarDatos: finalizado")
    }

    @Test
    fun testCreateAndGetReview() = runTest {
        Log.d(logTag, "testCreateAndGetReview: inicio")
        // Arrange
        val review = CreateReviewDto(
            userId = "user_test",
            articleId = testArticleId,
            rating = 4,
            text = "Me gustó",
            createdAt = "2026-10-28T00:00:00Z"
        )

        // Act
        Log.d(logTag, "testCreateAndGetReview: antes de createReview")
        datasource.createReview(review)
        Log.d(logTag, "testCreateAndGetReview: createReview terminado; antes de getAllReviews")
        val reviews = datasource.getAllReviews()
        Log.d(logTag, "testCreateAndGetReview: getAllReviews terminado (${reviews.size} resultados)")
        val savedReviews = reviews.filter {
            it.articleIdKey() == review.articleId && it.authorIdKey() == review.userId
        }

        // Assert
        // El resultado conserva el autor, la calificacion y el texto enviados.
        Truth.assertThat(savedReviews).hasSize(1)
        val savedReview = savedReviews[0].toReviewData(null)
        Truth.assertThat(savedReview.id).isNotEmpty()
        Truth.assertThat(savedReview.authorId).isEqualTo(review.userId)
        Truth.assertThat(savedReview.rating).isEqualTo(review.rating.toFloat())
        Truth.assertThat(savedReview.reviewText).isEqualTo(review.text.orEmpty())
        Unit
    }

    @Test
    fun testCreateCommentWithTransaction() = runTest {
        Log.d(logTag, "testCreateCommentWithTransaction: inicio")
        // Arrange
        val reviewRef = db.collection("reviews").document("review_comment_test")
        Log.d(logTag, "testCreateCommentWithTransaction: antes de sembrar resena")
        reviewRef.set(ReviewFirestoreDto(reviewId = reviewRef.id, commentsCount = 0)).await()
        Log.d(logTag, "testCreateCommentWithTransaction: resena sembrada")
        val comment = CreateCommentDto(
            userId = "user_test",
            username = "Usuario de prueba",
            text = "Comentario transaccional",
            createdAt = "2026-10-28T00:00:00Z"
        )

        // Act
        // La transaccion crea el comentario y aumenta el contador de la resena padre.
        Log.d(logTag, "testCreateCommentWithTransaction: antes de transaccion")
        val commentId = datasource.createComment(reviewRef.id, comment)
        Log.d(logTag, "testCreateCommentWithTransaction: transaccion terminada")

        // Assert
        // Comprueba el comentario nuevo en la subcoleccion y el contador actualizado.
        val commentSnapshot = reviewRef.collection("comments").document(commentId).get().await()
        Log.d(logTag, "testCreateCommentWithTransaction: comentario leido")
        val reviewSnapshot = reviewRef.get().await()
        Log.d(logTag, "testCreateCommentWithTransaction: resena padre leida")
        Truth.assertThat(commentSnapshot.exists()).isTrue()
        Truth.assertThat(commentSnapshot.getString("text")).isEqualTo(comment.text)
        Truth.assertThat(reviewSnapshot.getLong("commentsCount")).isEqualTo(1L)
        Unit
    }

    @Test
    fun testToggleReviewLike() = runTest {
        Log.d(logTag, "testToggleReviewLike: inicio")
        // Arrange
        val reviewRef = db.collection("reviews").document("review_like_test")
        Log.d(logTag, "testToggleReviewLike: antes de sembrar resena")
        reviewRef.set(ReviewFirestoreDto(reviewId = reviewRef.id, likesCount = 0)).await()
        Log.d(logTag, "testToggleReviewLike: resena sembrada")

        // Act
        // La primera llamada agrega el like y la segunda lo elimina.
        Log.d(logTag, "testToggleReviewLike: antes de primera transaccion")
        val likedState = datasource.toggleReviewLike(reviewRef.id, "user_test")
        Log.d(logTag, "testToggleReviewLike: primera transaccion terminada")
        val likedSnapshot = reviewRef.get().await()
        Log.d(logTag, "testToggleReviewLike: primer estado leido")
        val unlikedState = datasource.toggleReviewLike(reviewRef.id, "user_test")
        Log.d(logTag, "testToggleReviewLike: segunda transaccion terminada")
        val unlikedSnapshot = reviewRef.get().await()
        Log.d(logTag, "testToggleReviewLike: segundo estado leido")

        // Assert
        // El estado devuelto, el contador y el documento del like coinciden tras ambas llamadas.
        Truth.assertThat(likedState.isLikedByMe).isTrue()
        Truth.assertThat(likedState.likesCount).isEqualTo(1)
        Truth.assertThat(likedSnapshot.getLong("likesCount")).isEqualTo(1L)
        Truth.assertThat(unlikedState.isLikedByMe).isFalse()
        Truth.assertThat(unlikedState.likesCount).isEqualTo(0)
        Truth.assertThat(unlikedSnapshot.getLong("likesCount")).isEqualTo(0L)
        Truth.assertThat(datasource.isReviewLikedByUser(reviewRef.id, "user_test")).isFalse()
        Log.d(logTag, "testToggleReviewLike: aserciones terminadas")
        Unit
    }
}
