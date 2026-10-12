package com.example.kloth.data.injection

import com.example.kloth.data.dataresource.ArticleRetrofitDataSourceImplementation
import com.example.kloth.data.dataresource.ArticleRemoteDataSource
import com.example.kloth.data.dataresource.CommentRetrofitDataSourceImplementation
import com.example.kloth.data.dataresource.CommentRemoteDataSource
import com.example.kloth.data.dataresource.ReviewRetrofitDataSourceImplementation
import com.example.kloth.data.dataresource.ReviewRemoteDataSource
import com.example.kloth.data.dataresource.UserRetrofitDataSourceImplementation
import com.example.kloth.data.dataresource.UserRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindArticleRemoteDataSource(
        implementation: ArticleRetrofitDataSourceImplementation
    ): ArticleRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindReviewRemoteDataSource(
        implementation: ReviewRetrofitDataSourceImplementation
    ): ReviewRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCommentRemoteDataSource(
        implementation: CommentRetrofitDataSourceImplementation
    ): CommentRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(
        implementation: UserRetrofitDataSourceImplementation
    ): UserRemoteDataSource
}
