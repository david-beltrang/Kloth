package com.example.kloth.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.data.local.ProductDetailData
import com.example.kloth.ui.components.ErrorState
import com.example.kloth.ui.screens.detail.components.descriptionProduct.GeneralInfoSection
import com.example.kloth.ui.screens.detail.components.image.HeroImageSection
import com.example.kloth.ui.screens.detail.components.review.RatingsAndReviewsSection

@Composable
fun ItemDetailScreenContent(
    // Variables de estado (Datos que vienen del ViewModel)
    product: ProductDetailData,
    isLoadingReviews: Boolean = false,
    reviewsError: String? = null,

    // Métodos para manejar el estado (Eventos que suben al ViewModel)
    onFavoriteClick: () -> Unit = {},
    onRetryReviews: () -> Unit = {},

    // Navegación y otros eventos
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onAddToCartClick: () -> Unit = {},
    onWriteReviewClick: () -> Unit = {},
    onLikeReviewClick: (String) -> Unit = {},
    onCommentReviewClick: (String) -> Unit = {},
    onProfileClick: (Int) -> Unit = {},

    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            HeroImageSection(
                imageModel = product.imageUrl ?: product.imageRes,
                isFavorite = product.isFavorite,
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                onShareClick = onShareClick
            )
        }
        item {
            GeneralInfoSection(
                categoryTag = product.categoryTag,
                price = product.price,
                title = product.title,
                brand = product.brand,
                colorName = product.colorName,
                categoryName = product.categoryName,
                description = product.description,
            )
        }
        item {
            when {
                isLoadingReviews && product.reviewsList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                reviewsError != null && product.reviewsList.isEmpty() -> {
                    ErrorState(
                        message = reviewsError,
                        onRetry = onRetryReviews
                    )
                }

                else -> {
                    RatingsAndReviewsSection(
                        averageRating = product.averageRating,
                        reviewsCountText = product.reviewsCountText,
                        reviewsList = product.reviewsList,
                        onWriteReviewClick = onWriteReviewClick,
                        onLikeReviewClick = onLikeReviewClick,
                        onCommentReviewClick = onCommentReviewClick,
                        onProfileClick = onProfileClick
                    )
                }
            }
        }
    }
}
