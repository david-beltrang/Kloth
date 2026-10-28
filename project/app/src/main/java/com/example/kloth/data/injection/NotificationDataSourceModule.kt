package com.example.kloth.data.injection

import com.example.kloth.data.datasource.implementation.firestore.NotificationFirestoreDataSourceImpl
import com.example.kloth.data.datasource.services.NotificationRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Vincula el contrato de notificaciones con su implementacion de Firestore para Hilt.
@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindNotificationRemoteDataSource(
        implementation: NotificationFirestoreDataSourceImpl
    ): NotificationRemoteDataSource
}
