package com.example.kloth.data.injection

import com.example.kloth.data.dataresource.services.ArticleRetrofitService
import com.example.kloth.data.dataresource.services.ReviewRetrofitService
import com.example.kloth.data.dataresource.services.UserRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

// Agrupa todo lo de Retrofit (FirebaseHiltModule agrupa lo de Firebase)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/") // backend local visto desde el emulador; http sin S y slash final
        .addConverterFactory(GsonConverterFactory.create()) // lee JSON
        .addConverterFactory(ScalarsConverterFactory.create()) // lee texto plano
        .build()

    // Hilt no sabe crear los servicios: se crean una sola vez con retrofit.create(...)
    @Singleton
    @Provides
    fun provideArticleService(retrofit: Retrofit): ArticleRetrofitService =
        retrofit.create(ArticleRetrofitService::class.java)

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserRetrofitService =
        retrofit.create(UserRetrofitService::class.java)

    @Singleton
    @Provides
    fun provideReviewService(retrofit: Retrofit): ReviewRetrofitService =
        retrofit.create(ReviewRetrofitService::class.java)
}
