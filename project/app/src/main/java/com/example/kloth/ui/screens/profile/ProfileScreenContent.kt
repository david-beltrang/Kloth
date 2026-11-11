package com.example.kloth.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import coil.compose.AsyncImage
import com.example.kloth.R
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ModelPic
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.ui.screens.detail.components.ProductCatalogCard
import com.example.kloth.ui.screens.feed.components.ProfileTabRow
import com.example.kloth.ui.screens.profile.components.ArticleOptionsMenu
import com.example.kloth.ui.screens.profile.components.ProfileHeaderSection
import com.example.kloth.ui.screens.modelPics.ModelPicState
import com.example.kloth.ui.screens.modelPics.components.ModelPicImageDialog

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
    modelPicState: ModelPicState = ModelPicState(),
    highlightedModelPicId: String? = null,
    onTabSelected: (Int) -> Unit,
    onModelPicFilterSelected: (ModelPicType) -> Unit = {},
    onUploadModelPicClick: () -> Unit = {},
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
    val gridState = rememberLazyGridState()

    LaunchedEffect(selectedTabIndex, highlightedModelPicId, modelPicState.visibleModelPics) {
        if (selectedTabIndex == 2 && highlightedModelPicId != null) {
            val resultIndex = modelPicState.visibleModelPics.indexOfFirst { it.id == highlightedModelPicId }
            if (resultIndex >= 0) gridState.animateScrollToItem(resultIndex + 2)
        }
    }

    var selectedModelPic by remember { mutableStateOf<ModelPic?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("profileScreen")
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
                    showModelPics = viewingOwnProfile,
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
        } else if (selectedTabIndex == 1) {
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
        } else if (selectedTabIndex == 2 && viewingOwnProfile) {
            item(span = { GridItemSpan(2) }) {
                ModelPicsControls(
                    state = modelPicState,
                    onFilterSelected = onModelPicFilterSelected,
                    onUploadClick = onUploadModelPicClick
                )
            }

            items(modelPicState.visibleModelPics, key = { it.id }) { modelPic ->
                ModelPicGridItem(
                    modelPic = modelPic,
                    highlighted = modelPic.id == highlightedModelPicId,
                    onClick = { selectedModelPic = modelPic }
                )
            }
        }
        }

        selectedModelPic?.let { modelPic ->
            ModelPicImageDialog(
                modelPic = modelPic,
                onDismiss = { selectedModelPic = null }
            )
        }
    }
}

@Composable
private fun ModelPicsControls(
    state: ModelPicState,
    onFilterSelected: (ModelPicType) -> Unit,
    onUploadClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.selectedFilter == ModelPicType.BASE,
                onClick = { onFilterSelected(ModelPicType.BASE) },
                label = { Text(stringResource(R.string.model_pics_filter_uploaded)) }
            )
            FilterChip(
                selected = state.selectedFilter == ModelPicType.GENERATED,
                onClick = { onFilterSelected(ModelPicType.GENERATED) },
                label = { Text(stringResource(R.string.model_pics_filter_generated)) }
            )
        }

        if (state.selectedFilter == ModelPicType.BASE) {
            Button(
                onClick = onUploadClick,
                enabled = !state.isUploadingBaseModelPic,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isUploadingBaseModelPic) {
                    CircularProgressIndicator()
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.model_pics_uploading))
                } else {
                    Text(stringResource(R.string.model_pics_upload))
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        state.errorMessage?.let { error ->
            Text(
                text = error.ifBlank { stringResource(R.string.model_pics_load_error) },
                color = MaterialTheme.colorScheme.error
            )
        }
        state.uploadErrorMessage?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        if (!state.isLoading && state.visibleModelPics.isEmpty()) {
            Text(
                text = stringResource(
                    if (state.selectedFilter == ModelPicType.BASE) {
                        R.string.model_pics_empty_base
                    } else {
                        R.string.model_pics_empty_generated
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ModelPicGridItem(
    modelPic: ModelPic,
    highlighted: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (highlighted) Modifier
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .padding(4.dp)
                else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AsyncImage(
            model = modelPic.imageUrl,
            contentDescription = modelPic.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
        )
        Text(
            text = modelPic.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2
        )
    }
}
