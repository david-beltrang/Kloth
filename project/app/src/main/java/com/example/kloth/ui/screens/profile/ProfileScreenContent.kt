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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.data.local.FakeArticle
import com.example.kloth.data.local.PostItem
import com.example.kloth.ui.screens.detail.components.ProductCatalogCard
import com.example.kloth.ui.screens.feed.components.ProfileTabRow
import com.example.kloth.ui.screens.profile.components.ArticleOptionsMenu
import com.example.kloth.ui.screens.profile.components.ProfileHeaderSection

@Composable
fun ProfileScreenContent(
    email: String,
    username: String?,
    bio: String?,
    location: String?,
    website: String?,
    profileImageUrl: String?,
    viewingOwnProfile: Boolean = true,
    followersCount: Int = 0,
    followingCount: Int = 0,
    isFollowing: Boolean = false,
    isFollowActionInProgress: Boolean = false,
    selectedTabIndex: Int,
    userArticles: List<PostItem>,
    reviewedArticles: List<PostItem>,
    onTabSelected: (Int) -> Unit,
    onEditProfileClick: () -> Unit,
    onFollowClick: () -> Unit = {},
    onProductClick: (String) -> Unit,
    onDeleteArticleClick: (PostItem) -> Unit,
    onEditArticleClick: (String) -> Unit,
    onLogoutClick: () -> Unit,
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {},
    modifier: Modifier = Modifier,
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                ProfileHeaderSection(
                    email = email,
                    username = username,
                    bio = bio,
                    location = location,
                    website = website,
                    profileImageUrl = profileImageUrl,
                    viewingOwnProfile = viewingOwnProfile,
                    publicationsCount = userArticles.size,
                    followersCount = followersCount,
                    followingCount = followingCount,
                    reviewsCount = reviewedArticles.size,
                    onEditProfileClick = onEditProfileClick,
                    isFollowing = isFollowing,
                    isFollowActionInProgress = isFollowActionInProgress,
                    onFollowClick = onFollowClick,
                    onLogoutClick = onLogoutClick,
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileTabRow(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = onTabSelected
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (selectedTabIndex == 0) {
            items(userArticles, key = { it.id }) { post ->
                val p = post.product
                Box {
                    ProductCatalogCard(
                        title = p.title,
                        category = p.categoryTag,
                        rating = p.averageRating.toString(),
                        reviews = p.reviewsCount.toString(),
                        imageModel = p.imageUrl ?: p.imageRes,
                        onClick = { onProductClick(p.id) },
                        showFavorite = false
                    )
                    if (viewingOwnProfile) ArticleOptionsMenu(
                        onEditClick = { onEditArticleClick(p.id) },
                        onDeleteClick = { onDeleteArticleClick(post) },
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                }
            }
        } else {
            items(reviewedArticles, key = { it.id }) { post ->
                val p = post.product
                ProductCatalogCard(
                    title = p.title,
                    category = p.categoryTag,
                    rating = p.averageRating.toString(),
                    reviews = p.reviewsCount.toString(),
                    imageModel = p.imageUrl ?: p.imageRes,
                    onClick = { onProductClick(p.id) },
                    showFavorite = false
                )
            }
        }
    }
}
