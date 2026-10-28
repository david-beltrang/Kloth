package com.example.kloth.repository

import com.example.kloth.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImpl
import com.example.kloth.data.datasource.implementation.firestore.UserFirestoreDataSourceImpl
import com.example.kloth.data.dto.ReviewFirestoreDto
import com.example.kloth.data.dto.UserFirestoreDto
import com.example.kloth.data.repository.ReviewRepository
import com.example.kloth.data.repository.UserRepository
import com.example.kloth.dataSourceTest.grantLocalNetworkPermissionForFirebaseEmulator
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ReviewRepositoryIntegrationTest {

    private val testArticleId = "article_review_repo_test"
    private val db = Firebase.firestore
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var reviewDataSource: ReviewFirestoreDataSourceImpl

    @Before
    fun setUp() = runTest {
        grantLocalNetworkPermissionForFirebaseEmulator()
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) {
        }

        reviewDataSource = ReviewFirestoreDataSourceImpl(db)
        reviewRepository = ReviewRepository(
            reviewDataSource,
            UserRepository(UserFirestoreDataSourceImpl(db))
        )
        limpiarDatos()
    }

    @After
    fun tearDown() = runTest {
        limpiarDatos()
    }

    private suspend fun limpiarDatos() {
        val reviews = db.collection("reviews")
            .whereEqualTo("articleId", testArticleId)
            .get()
            .await()
        for (review in reviews) reviewDataSource.deleteReview(review.id)
        reviewDataSource.deleteReview("review_repo_detail")
        val users = db.collection("users").get().await()
        for (user in users) user.reference.delete().await()
    }

    @Test
    fun createReview_validData_success() = runTest {
        // arrange
        val userId = "review_author_1"
        db.collection("users").document(userId).set(
            UserFirestoreDto(
                userId = userId,
                username = "reviewer_1",
                email = "reviewer1@example.com",
                fullName = "Reviewer One"
            )
        ).await()

        // act
        val result = reviewRepository.createReview(
            articleId = testArticleId,
            userId = userId,
            rating = 4,
            text = "Me gustó la prenda"
        )
        val allReviews = reviewRepository.getAllReviews()

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(allReviews.isSuccess).isTrue()
        val saved = allReviews.getOrNull()?.filter {
            it.authorId == userId && it.reviewText == "Me gustó la prenda"
        }
        Truth.assertThat(saved).hasSize(1)
        Truth.assertThat(saved?.first()?.authorName).isEqualTo("reviewer_1")
        Truth.assertThat(saved?.first()?.rating).isEqualTo(4f)
    }

    @Test
    fun getReviewById_validId_returnsReviewWithAuthor() = runTest {
        // arrange
        val userId = "review_author_2"
        val reviewId = "review_repo_detail"
        db.collection("users").document(userId).set(
            UserFirestoreDto(
                userId = userId,
                username = "autor_resena",
                email = "autor@example.com",
                fullName = "Autor Resena",
                imageURL = "https://example.com/avatar.jpg"
            )
        ).await()
        db.collection("reviews").document(reviewId).set(
            ReviewFirestoreDto(
                reviewId = reviewId,
                userId = userId,
                articleId = testArticleId,
                rating = 5,
                text = "Excelente",
                likesCount = 0,
                commentsCount = 0,
                createdAt = "2026-10-28T00:00:00Z"
            )
        ).await()

        // act
        // dto -> mapper -> ui (reseña + autor -> ReviewData)
        val result = reviewRepository.getReviewById(reviewId)

        // assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo(reviewId)
        Truth.assertThat(result.getOrNull()?.authorId).isEqualTo(userId)
        Truth.assertThat(result.getOrNull()?.authorName).isEqualTo("autor_resena")
        Truth.assertThat(result.getOrNull()?.reviewText).isEqualTo("Excelente")
        Truth.assertThat(result.getOrNull()?.rating).isEqualTo(5f)
        Truth.assertThat(result.getOrNull()?.avatarUrl).isEqualTo("https://example.com/avatar.jpg")
    }
}
