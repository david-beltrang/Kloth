package com.example.kloth.data.dataresource

import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewDto
import com.example.kloth.data.dto.UpdateReviewDto

interface ReviewRemoteDataSource {
    suspend fun getAllReviews(): List<ReviewDto>
    suspend fun getReviewById(id: String): ReviewDto
    suspend fun getReviewsByArticle(articleId: String): List<ReviewDto>
    suspend fun getReviewsByUser(userId: String): List<ReviewDto>
    suspend fun createReview(dto: CreateReviewDto): ReviewDto
    suspend fun updateReview(id: String, dto: UpdateReviewDto): ReviewDto
    suspend fun deleteReview(id: String)
}
