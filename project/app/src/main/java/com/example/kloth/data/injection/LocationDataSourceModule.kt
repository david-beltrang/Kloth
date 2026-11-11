package com.example.kloth.data.injection

import com.example.kloth.data.datasource.implementation.location.FusedLocationDataSourceImpl
import com.example.kloth.data.datasource.services.LocationDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Vincula el contrato de ubicación con su implementación de FusedLocationProvider para Hilt.
@Module
@InstallIn(SingletonComponent::class)
abstract class LocationDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindLocationDataSource(
        implementation: FusedLocationDataSourceImpl
    ): LocationDataSource
}
