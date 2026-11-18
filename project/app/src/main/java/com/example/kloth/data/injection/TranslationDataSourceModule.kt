package com.example.kloth.data.injection

import com.example.kloth.data.datasource.implementation.mlkit.TranslationMlKitDataSource
import com.example.kloth.data.datasource.services.TranslationRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TranslationDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindTranslationRemoteDataSource(
        implementation: TranslationMlKitDataSource
    ): TranslationRemoteDataSource
}
