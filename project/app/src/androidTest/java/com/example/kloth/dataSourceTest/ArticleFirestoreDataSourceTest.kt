package com.example.kloth.dataSourceTest

import com.example.kloth.data.datasource.implementation.firestore.ArticleFirestoreDataSourceImpl
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.CreateReviewDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ArticleFirestoreDataSourceTest {

    private val db = Firebase.firestore
    private lateinit var datasource: ArticleFirestoreDataSourceImpl

    @Before
    fun setup() = runTest {
        grantLocalNetworkPermissionForFirebaseEmulator()
        datasource = ArticleFirestoreDataSourceImpl(db)
        // Inicia con colecciones vacias para aislar los resultados de ejecuciones anteriores.
        limpiarDatos()
    }

    @After
    fun tearDown() = runTest {
        // Elimina los documentos de prueba al terminar cada caso.
        limpiarDatos()
    }

    private suspend fun limpiarDatos() {
        // Limpia las colecciones usadas por las pruebas de articulos.
        val reviews = db.collection("reviews").get().await()
        for (review in reviews) review.reference.delete().await()
        val articles = db.collection("articles").get().await()
        for (article in articles) article.reference.delete().await()
    }

    @Test
    fun testCreateAndGetAllArticles() = runTest {
        // Arrange
        val article = CreateArticleDto(
            typeArticle = "prenda",
            title = "Chaqueta de prueba",
            description = "Chaqueta para integration test",
            idUser = "user_test",
            imageArticle = "https://example.com/chaqueta.jpg",
            createdAt = "2026-10-28T00:00:00Z",
            tags = listOf("prueba", "chaqueta")
        )

        // Act
        // El datasource guarda el articulo y luego vuelve a leer la coleccion de Firestore.
        datasource.createArticle(article)
        val articles = datasource.getAllArticles()

        // Assert
        // La lista contiene el titulo guardado y el ID asignado por Firestore.
        Truth.assertThat(articles).hasSize(1)
        val savedArticle = articles[0].toArticleInfo()
        Truth.assertThat(savedArticle.id).isNotEmpty()
        Truth.assertThat(savedArticle.title).isEqualTo(article.title)
    }

    @Test
    fun testGetArticleById() = runTest {
        // Arrange
        val article = CreateArticleDto(
            typeArticle = "prenda",
            title = "Artículo por ID",
            description = "Descripción",
            idUser = "user_test",
            imageArticle = "https://example.com/item.jpg",
            createdAt = "2026-10-28T00:00:00Z"
        )
        val articleRef = db.collection("articles").document()
        // Se crea el documento directamente para consultarlo luego con un ID conocido.
        articleRef.set(article).await()

        // Act
        val result = datasource.getArticleById(articleRef.id).toArticleInfo()

        // Assert
        // El resultado mapeado conserva el ID del documento y el titulo del articulo.
        Truth.assertThat(result.id).isEqualTo(articleRef.id)
        Truth.assertThat(result.title).isEqualTo(article.title)
    }

    @Test
    fun testDeleteArticleWithCascade() = runTest {
        // Arrange
        val articleRef = db.collection("articles").document("article_cascade")
        articleRef.set(
            CreateArticleDto(
                typeArticle = "prenda",
                title = "Artículo con reseñas",
                description = "Descripción",
                idUser = "user_test",
                imageArticle = "https://example.com/item.jpg",
                createdAt = "2026-10-28T00:00:00Z"
            )
        ).await()
        val reviewRef = db.collection("reviews").document("review_cascade")
        reviewRef.set(
            CreateReviewDto(
                userId = "user_test",
                articleId = articleRef.id,
                rating = 5,
                text = "Buena prenda",
                createdAt = "2026-10-28T00:00:00Z"
            )
        ).await()

        // Act
        // Al borrar el articulo, el datasource tambien debe borrar la resena asociada.
        datasource.deleteArticle(articleRef.id)

        // Assert
        // Se espera que ambos documentos ya no existan en Firestore.
        Truth.assertThat(articleRef.get().await().exists()).isFalse()
        Truth.assertThat(reviewRef.get().await().exists()).isFalse()
    }
}
