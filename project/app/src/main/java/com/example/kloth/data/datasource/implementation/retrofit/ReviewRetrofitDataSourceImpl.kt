package com.example.kloth.data.datasource.implementation.retrofit

import com.example.kloth.data.datasource.services.ReviewRemoteDataSource
import com.example.kloth.data.datasource.services.ReviewRetrofitService
import com.example.kloth.data.dto.CreateReviewDto
import com.example.kloth.data.dto.ReviewDtoGeneric
import com.example.kloth.data.dto.ReviewRetrofitDto
import com.example.kloth.data.dto.UpdateReviewDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {
    override suspend fun getAllReviews(): List<ReviewRetrofitDto> = service.getReviews()
    override suspend fun getReviewById(id: String): ReviewRetrofitDto {
        TODO("Not yet implemented")
    }

    override suspend fun createReview(dto: CreateReviewDto) {
        TODO("Not yet implemented")
    }

    override suspend fun updateReview(
        id: String,
        dto: UpdateReviewDto
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReview(id: String) {
        TODO("Not yet implemented")
    }
}
