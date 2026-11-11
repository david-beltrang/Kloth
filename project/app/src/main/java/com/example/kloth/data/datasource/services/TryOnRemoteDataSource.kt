package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.TryOnResultDto

interface TryOnRemoteDataSource {
    suspend fun generateTryOn(publicationId: String, baseModelPicId: String): TryOnResultDto
}
