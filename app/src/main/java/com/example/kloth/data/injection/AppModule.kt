package com.example.kloth.data.injection

import com.example.kloth.data.services.ArticleRetrofitService
import com.example.kloth.data.services.CommentRetrofitService
import com.example.kloth.data.services.ReviewRetrofitService
import com.example.kloth.data.services.UserRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://localhost:3000/"

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesArticleRetrofitService(retrofit: Retrofit): ArticleRetrofitService {
        return retrofit.create(ArticleRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesReviewRetrofitService(retrofit: Retrofit): ReviewRetrofitService {
        return retrofit.create(ReviewRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesCommentRetrofitService(retrofit: Retrofit): CommentRetrofitService {
        return retrofit.create(CommentRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesUserRetrofitService(retrofit: Retrofit): UserRetrofitService {
        return retrofit.create(UserRetrofitService::class.java)
    }
}
