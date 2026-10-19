package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.services.ArticleRemoteDataSource
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.ArticleFirestoreDto
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.ReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class ArticleFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): ArticleRemoteDataSource{
    override suspend fun getAllArticles(): List<ArticleDtoGeneric> {
        return db.collection("articles").get().await().documents.mapNotNull { doc ->
            doc.toObject(ArticleFirestoreDto::class.java)?.copy(articleId = doc.id)
        }
    }

    override suspend fun getArticleById(id: String): ArticleDtoGeneric {
        val doc = db.collection("articles").document(id).get().await()
        return doc.toObject(ArticleFirestoreDto::class.java)?.copy(articleId = doc.id)
            ?: throw Exception("Artículo no encontrado")
    }

    override suspend fun createArticle(article: CreateArticleDto){
        db.collection("articles").add(article).await()
    }

    override suspend fun getArticleReviews(id: String): List<ReviewDto> {
        return emptyList()
    }

}