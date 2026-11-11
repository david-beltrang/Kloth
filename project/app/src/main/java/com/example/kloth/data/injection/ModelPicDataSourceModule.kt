package com.example.kloth.data.injection

import com.example.kloth.data.datasource.implementation.firestore.ModelPicFirestoreDataSource
import com.example.kloth.data.datasource.services.ModelPicRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ModelPicDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindModelPicRemoteDataSource(
        dataSource: ModelPicFirestoreDataSource
    ): ModelPicRemoteDataSource
}
