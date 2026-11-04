package com.example.kloth.data.injection

import android.os.Build
import com.example.kloth.data.datasource.services.ArticleRetrofitService
import com.example.kloth.data.datasource.services.ReviewRetrofitService
import com.example.kloth.data.datasource.services.UserRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

// Agrupa todo lo de Retrofit (FirebaseHiltModule agrupa lo de Firebase)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Si estamos en un emulador, la IP del host es 10.0.2.2 de lo contrario 127.0.0.1
    private val BASE_HOST: String
    get() = if (isEmulator()) "10.0.2.2" else "127.0.0.1"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://$BASE_HOST:3000/")
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

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    }
}
