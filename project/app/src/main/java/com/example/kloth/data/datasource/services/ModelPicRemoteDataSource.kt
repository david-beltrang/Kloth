package com.example.kloth.data.datasource.services

import com.example.kloth.data.dto.ModelPicFirestoreDto

interface ModelPicRemoteDataSource {
    suspend fun getModelPics(userId: String): List<ModelPicFirestoreDto>

    suspend fun createBaseModelPic(
        userId: String,
        name: String,
        imageUrl: String,
        storagePath: String
    ): String
}
