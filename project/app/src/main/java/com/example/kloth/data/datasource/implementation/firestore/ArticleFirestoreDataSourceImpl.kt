package com.example.kloth.data.datasource.implementation.firestore

import com.example.kloth.data.datasource.services.ArticleRemoteDataSource
import com.example.kloth.data.dto.ArticleDtoGeneric
import com.example.kloth.data.dto.ArticleFirestoreDto
import com.example.kloth.data.dto.CreateArticleDto
import com.example.kloth.data.dto.ReviewDtoGeneric
import com.example.kloth.data.dto.ReviewFirestoreDto
import com.example.kloth.data.dto.UpdateArticleDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class ArticleFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): ArticleRemoteDataSource{
    override suspend fun getAllArticles(): List<ArticleDtoGeneric> {
        val snapshot = db.collection("articles").get().await()

        return snapshot.documents.map { doc ->
            val article = doc.toObject(ArticleFirestoreDto::class.java)
            article?.copy(articleId = doc.id) ?: throw Exception("Article not found")
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

    override suspend fun getArticleReviews(id: String): List<ReviewDtoGeneric> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("articleId", id)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(ReviewFirestoreDto::class.java)?.copy(reviewId = doc.id)
        }
    }

    override suspend fun updateArticle(id: String, article: UpdateArticleDto) {
        db.collection("articles").document(id).set(article, SetOptions.merge()).await()
    }

    override suspend fun deleteArticle(id: String) {
        db.collection("articles").document(id).delete().await()
    }

}