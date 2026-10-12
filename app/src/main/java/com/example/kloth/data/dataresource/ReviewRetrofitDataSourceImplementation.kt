package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewDto
import com.example.kloth.data.dto.UpdateReviewDto
import com.example.kloth.data.services.ReviewRetrofitService
import javax.inject.Inject

class ReviewRetrofitDataSourceImplementation @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {

    override suspend fun getAllReviews(): List<ReviewDto> = service.getAllReviews()

    override suspend fun getReviewById(id: String): ReviewDto = service.getReviewById(id)

    override suspend fun getReviewsByArticle(articleId: String): List<ReviewDto> =
        service.getReviewsByArticle(articleId)

    override suspend fun getReviewsByUser(userId: String): List<ReviewDto> =
        service.getReviewsByUser(userId)

    override suspend fun createReview(dto: CreateReviewDto): ReviewDto = service.createReview(dto)

    override suspend fun updateReview(id: String, dto: UpdateReviewDto): ReviewDto =
        service.updateReview(id, dto)

    override suspend fun deleteReview(id: String) = service.deleteReview(id)
}
