package com.example.kloth.data.datasource.implementation.retrofit

import com.example.kloth.data.datasource.services.ReviewRemoteDataSource
import com.example.kloth.data.datasource.services.ReviewRetrofitService
import com.example.kloth.data.dto.ReviewDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {
    override suspend fun getAllReviews(): List<ReviewDto> = service.getReviews()
}
