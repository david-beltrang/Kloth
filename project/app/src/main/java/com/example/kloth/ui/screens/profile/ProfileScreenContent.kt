package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ReviewData
import com.example.kloth.ui.screens.detail.components.ProductCatalogCard
import com.example.kloth.ui.screens.detail.components.review.ReviewItem
import com.example.kloth.ui.screens.feed.components.ProfileTabRow
import com.example.kloth.ui.screens.profile.components.ProfileHeaderSection

@Composable
fun ProfileScreenContent(
    email: String,
    profileImageUrl: String?,
    profileUserName: String? = null,
    isCurrentUser: Boolean = true,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit = {},
    userArticles: List<PostItem> = emptyList(),
    userReviews: List<ReviewData> = emptyList(),
    isLoadingArticles: Boolean = false,
    isLoadingReviews: Boolean = false,
    onProductClick: (String) -> Unit = {},
    onCommentReviewClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 24.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {

        item(span = { GridItemSpan(2) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)
                ) {
                    ProfileHeaderSection(
                        email = email,
                        profileImageUrl = profileImageUrl,
                        displayName = profileUserName,
                        showActions = isCurrentUser,
                        onEditProfileClick = onEditProfileClick,
                        onLogoutClick = onLogoutClick
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileTabRow(
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = onTabSelected
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (!isCurrentUser) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        if (selectedTabIndex == 0) {
            // Publicaciones: artículos del usuario
            if (isLoadingArticles) {
                item(span = { GridItemSpan(2) }) {
                    LoadingState()
                }
            } else if (userArticles.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    EmptyState("No hay publicaciones aún")
                }
            } else {
                items(userArticles, key = { it.id }) { postItem ->
                    ProductCatalogCard(
                        title = postItem.product.title,
                        category = postItem.product.categoryTag,
                        rating = postItem.product.averageRating.toString(),
                        reviews = postItem.product.reviewsCountText.filter { it.isDigit() },
                        imageModel = postItem.product.imageUrl ?: postItem.product.imageRes,
                        onClick = { onProductClick(postItem.product.id) }
                    )
                }
            }
        } else {
            // Mis reseñas: reseñas del usuario
            if (isLoadingReviews) {
                item(span = { GridItemSpan(2) }) {
                    LoadingState()
                }
            } else if (userReviews.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    EmptyState("Aún no has escrito reseñas")
                }
            } else {
                items(userReviews, key = { it.id }, span = { GridItemSpan(2) }) { review ->
                    ReviewItem(
                        review = review,
                        onClick = { onProductClick(review.productId) },
                        onCommentClick = { onCommentReviewClick(review.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(text: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}