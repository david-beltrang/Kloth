package com.example.kloth.data.injection

import com.example.kloth.data.datasource.implementation.firebase.TryOnFirebaseDataSource
import com.example.kloth.data.datasource.services.TryOnRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TryOnDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindTryOnRemoteDataSource(
        dataSource: TryOnFirebaseDataSource
    ): TryOnRemoteDataSource
}
