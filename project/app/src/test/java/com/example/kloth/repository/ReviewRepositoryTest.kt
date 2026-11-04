package com.example.kloth.repository

import com.example.kloth.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImpl
import com.example.kloth.data.dto.ReviewFirestoreDto
import com.example.kloth.data.repository.ReviewRepository
import com.example.kloth.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

// Pruebas unitarias del repositorio de reseñas (carpeta test): usan MockK, sin emulador.
class ReviewRepositoryTest {

    private val mockDataSource = mockk<ReviewFirestoreDataSourceImpl>()
    private val mockUserRepository = mockk<UserRepository>()
    private val repository = ReviewRepository(
        reviewDataSource = mockDataSource,
        userRepository = mockUserRepository
    )

    @Test
    fun getAllReviews_validData_returnsReviewsWithMappings() = runTest {
        // arrange
        val dto = ReviewFirestoreDto(
            reviewId = "rev_1",
            articleId = "art_1",
            userId = "user_1",
            rating = 5,
            text = "Excellent review!",
            createdAt = "2024-01-01T00:00:00.000Z",
            eliminated = false
        )
        coEvery { mockDataSource.getAllReviews() } returns listOf(dto)
        coEvery { mockUserRepository.getAllUsers() } returns listOf(
            com.example.kloth.data.dto.UserFirestoreDto(
                userId = "user_1",
                username = "Author Name",
                email = "author@test.com",
                fullName = "Author Name",
                biography = "Bio",
                imageURL = "https://img.test/a.jpg",
                followersCount = 100,
                followingCount = 50
            )
        )

        // act
        val result = repository.getAllReviews()

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.size).isEqualTo(1)
        val review = result.getOrNull()?.first()
        Truth.assertThat(review?.id).isEqualTo("rev_1")
        Truth.assertThat(review?.rating).isEqualTo(5)
        Truth.assertThat(review?.authorName).isEqualTo("Author Name")
    }

    @Test
    fun getReviewById_validId_returnsReviewWithAuthor() = runTest {
        // arrange
        val dto = ReviewFirestoreDto(
            reviewId = "rev_1",
            articleId = "art_1",
            userId = "user_1",
            rating = 4,
            text = "Good review",
            createdAt = "2024-01-01T00:00:00.000Z",
            eliminated = false
        )
        coEvery { mockDataSource.getReviewById("rev_1") } returns dto
        coEvery { mockDataSource.getComments("rev_1") } returns emptyList()
        coEvery { mockUserRepository.getAllUsers() } returns listOf(
            com.example.kloth.data.dto.UserFirestoreDto(
                userId = "user_1",
                username = "Author Name",
                email = "author@test.com",
                fullName = "Author Name",
                biography = "Bio",
                imageURL = "https://img.test/a.jpg",
                followersCount = 100,
                followingCount = 50
            )
        )

        // act
        val result = repository.getReviewById("rev_1")

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        val review = result.getOrNull()
        Truth.assertThat(review?.id).isEqualTo("rev_1")
        Truth.assertThat(review?.rating).isEqualTo(4)
        Truth.assertThat(review?.authorName).isEqualTo("Author Name")
    }

    @Test
    fun createReview_validDto_success() = runTest {
        // arrange
        coEvery { mockDataSource.createReview(any()) } returns Unit

        // act
        val result = repository.createReview(
            articleId = "art_1",
            userId = "user_1",
            rating = 5,
            text = "Great review!"
        )

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun toggleReviewLike_validId_togglesLike() = runTest {
        // arrange
        coEvery { mockDataSource.toggleReviewLike("rev_1", "user_1") } returns com.example.kloth.data.local.ReviewLikeState(isLikedByMe = true, likesCount = 10)

        // act
        val result = repository.toggleReviewLike("rev_1", "user_1")

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.isLikedByMe).isTrue()
        Truth.assertThat(result.getOrNull()?.likesCount).isEqualTo(10)
    }
}
