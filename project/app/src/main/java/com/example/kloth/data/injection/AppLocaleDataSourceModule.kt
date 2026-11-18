package com.example.kloth.data.injection

import com.example.kloth.data.datasource.implementation.platform.AppLocaleDataSourceImpl
import com.example.kloth.data.datasource.services.AppLocaleDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppLocaleDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAppLocaleDataSource(
        implementation: AppLocaleDataSourceImpl
    ): AppLocaleDataSource
}
