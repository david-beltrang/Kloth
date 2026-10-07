package com.example.kloth.data.dataresource.implementation

import com.example.kloth.data.dataresource.ReviewRemoteDataSource
import com.example.kloth.data.dataresource.services.ReviewRetrofitService
import com.example.kloth.data.dto.ReviewDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {
    override suspend fun getAllReviews(): List<ReviewDto> = service.getReviews()
}
