package com.example.kloth.repository

import com.example.kloth.data.datasource.implementation.firestore.ArticleFirestoreDataSourceImpl
import com.example.kloth.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImpl
import com.example.kloth.data.dto.ArticleFirestoreDto
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.UserFirestoreDto
import com.example.kloth.data.repository.ArticleRepository
import com.example.kloth.data.repository.StorageRepository
import com.example.kloth.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

// Pruebas unitarias del repositorio de artículos (carpeta test): usan MockK, sin emulador.
class ArticleRepositoryTest {

    private val mockDataSource = mockk<ArticleFirestoreDataSourceImpl>()
    private val mockUserRepository = mockk<UserRepository>()
    private val mockReviewDataSource = mockk<ReviewFirestoreDataSourceImpl>()
    private val mockStorageRepository = mockk<StorageRepository>()
    private val repository = ArticleRepository(
        articleDataSource = mockDataSource,
        userRepository = mockUserRepository,
        reviewDataSource = mockReviewDataSource,
        storageRepository = mockStorageRepository
    )

    @Test
    fun getFeedPosts_validData_returnsPosts() = runTest {
        // arrange
        val dto = ArticleFirestoreDto(
            articleId = "art_1",
            typeArticle = "PRENDA",
            title = "Test Article",
            description = "Test content",
            createdAt = "2024-01-01T00:00:00.000Z",
            idUser = "user_1",
            eliminated = false,
            imageArticle = "https://img.test/img.jpg"
        )
        coEvery { mockDataSource.getAllArticles() } returns listOf(dto)
        coEvery { mockUserRepository.getAllUsers() } returns listOf(UserFirestoreDto(
            userId = "user_1",
            username = "test_user",
            email = "test@test.com",
            fullName = "Test User",
            biography = "Bio",
            imageURL = "https://img.test/a.jpg",
            followersCount = 10,
            followingCount = 5
        ))
        coEvery { mockReviewDataSource.getAllReviews() } returns emptyList()

        // act
        val result = repository.getFeedPosts()

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.size).isEqualTo(1)
        Truth.assertThat(result.getOrNull()?.first()?.id).isEqualTo("art_1")
    }

    @Test
    fun getArticleDetail_validId_returnsArticleWithReviews() = runTest {
        // arrange
        val dto = ArticleFirestoreDto(
            articleId = "art_1",
            typeArticle = "PRENDA",
            title = "Test Article",
            description = "Test content",
            createdAt = "2024-01-01T00:00:00.000Z",
            idUser = "user_1",
            eliminated = false,
            imageArticle = "https://img.test/img.jpg"
        )
        val reviewDto = com.example.kloth.data.dto.ReviewFirestoreDto(
            reviewId = "rev_1",
            articleId = "art_1",
            userId = "user_2",
            rating = 5,
            text = "Great article!",
            createdAt = "2024-01-02T00:00:00.000Z",
            eliminated = false
        )
        coEvery { mockDataSource.getArticleById("art_1") } returns dto
        coEvery { mockDataSource.getArticleReviews("art_1") } returns listOf(reviewDto)
        coEvery { mockUserRepository.getAllUsers() } returns listOf(UserFirestoreDto(
            userId = "user_2",
            username = "reviewer",
            email = "rev@test.com",
            fullName = "Reviewer",
            biography = "Bio",
            imageURL = "https://img.test/b.jpg",
            followersCount = 5,
            followingCount = 2
        ))

        // act
        val result = repository.getArticleDetail("art_1")

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo("art_1")
    }

    @Test
    fun createArticle_validDto_success() = runTest {
        // arrange
        val dto = CreateArticleDto(
            typeArticle = "PRENDA",
            title = "New Article",
            description = "Content",
            idUser = "user_1",
            imageArticle = "https://img.test/img.jpg",
            createdAt = "2024-01-01T00:00:00.000Z"
        )
        coEvery { mockDataSource.createArticle(dto) } returns Unit

        // act
        val result = repository.createArticle(dto)

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun deleteArticle_validId_success() = runTest {
        // arrange
        coEvery { mockDataSource.deleteArticle("art_1") } returns Unit
        coEvery { mockStorageRepository.deleteImage("https://img.test/img.jpg") } returns Result.success(Unit)

        // act
        val result = repository.deleteArticle("art_1", "https://img.test/img.jpg")

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
    }
}
