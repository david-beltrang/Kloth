package com.example.kloth.repository

import androidx.test.platform.app.InstrumentationRegistry
import com.example.kloth.data.datasource.implementation.firestore.ArticleFirestoreDataSourceImpl
import com.example.kloth.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImpl
import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.datasource.services.AuthRemoteDataSource
import com.example.kloth.data.datasource.services.StorageRemoteDataSource
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.UserFirestoreDto
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.dataSourceTest.grantLocalNetworkPermissionForFirebaseEmulator
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ArticleRepositoryIntegrationTest {

    private val db = Firebase.firestore
    private lateinit var articleRepository: ArticleRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() = runTest {
        grantLocalNetworkPermissionForFirebaseEmulator()
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {
        }

        // Inyeccion manual (sin Hilt / sin MockK), como en clase
        userRepository = UserRepository(UserFirestoreDataSourceImpl(db))
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val storageRepository = StorageRepository(
            StorageRemoteDataSource(Firebase.storage),
            AuthRemoteDataSource(Firebase.auth),
            context
        )
        articleRepository = ArticleRepository(
            ArticleFirestoreDataSourceImpl(db),
            userRepository,
            ReviewFirestoreDataSourceImpl(db),
            storageRepository
        )
        limpiarDatos()
    }

    @After
    fun tearDown() = runTest {
        limpiarDatos()
    }

    private suspend fun limpiarDatos() {
        val reviews = db.collection("reviews").get().await()
        for (review in reviews) review.reference.delete().await()
        val articles = db.collection("articles").get().await()
        for (article in articles) article.reference.delete().await()
        val users = db.collection("users").get().await()
        for (user in users) user.reference.delete().await()
    }

    private suspend fun seedUser(userId: String, username: String = "autor_test") {
        db.collection("users").document(userId).set(
            UserFirestoreDto(
                userId = userId,
                username = username,
                email = "$userId@example.com",
                fullName = username
            )
        ).await()
    }

    @Test
    fun createArticle_validDto_success() = runTest {
        // arrange
        val userId = "article_author_1"
        seedUser(userId)
        val article = CreateArticleDto(
            typeArticle = "prenda",
            title = "Chaqueta de prueba",
            description = "Descripcion de integration test",
            idUser = userId,
            imageArticle = "https://example.com/chaqueta.jpg",
            createdAt = "2026-10-28T00:00:00Z",
            tags = listOf("prueba")
        )

        // act
        val result = articleRepository.createArticle(article)
        val feed = articleRepository.getFeedPosts()

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(feed.isSuccess).isTrue()
        Truth.assertThat(feed.getOrNull()).hasSize(1)
        Truth.assertThat(feed.getOrNull()?.first()?.product?.title).isEqualTo(article.title)
    }

    @Test
    fun getFeedPosts_withArticles_returnsPostItems() = runTest {
        // arrange
        val userId = "article_author_2"
        seedUser(userId, username = "creador_feed")
        articleRepository.createArticle(
            CreateArticleDto(
                typeArticle = "prenda",
                title = "Post en el feed",
                description = "Desc",
                idUser = userId,
                imageArticle = "https://example.com/item.jpg",
                createdAt = "2026-10-28T00:00:00Z"
            )
        )

        // act
        // dto -> mapper -> ui (articulo + autor -> PostItem)
        val result = articleRepository.getFeedPosts()

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).hasSize(1)
        Truth.assertThat(result.getOrNull()?.first()?.creatorName).isEqualTo("creador_feed")
        Truth.assertThat(result.getOrNull()?.first()?.product?.title).isEqualTo("Post en el feed")
        Truth.assertThat(result.getOrNull()?.first()?.creatorId).isEqualTo(userId)
    }

    @Test
    fun getFeedPosts_emptyDatabase_returnsEmptyList() = runTest {
        // arrange
        // base limpia en setUp

        // act
        val result = articleRepository.getFeedPosts()

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun getArticleDetail_validId_returnsArticleInfo() = runTest {
        // arrange
        val userId = "article_author_3"
        seedUser(userId)
        articleRepository.createArticle(
            CreateArticleDto(
                typeArticle = "prenda",
                title = "Detalle articulo",
                description = "Para detalle",
                idUser = userId,
                imageArticle = "https://example.com/detail.jpg",
                createdAt = "2026-10-28T00:00:00Z"
            )
        )
        val articleId = articleRepository.getFeedPosts().getOrNull()?.first()?.id
        Truth.assertThat(articleId).isNotNull()

        // act
        val result = articleRepository.getArticleDetail(articleId!!)

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo(articleId)
        Truth.assertThat(result.getOrNull()?.title).isEqualTo("Detalle articulo")
        Truth.assertThat(result.getOrNull()?.creatorId).isEqualTo(userId)
    }
}
