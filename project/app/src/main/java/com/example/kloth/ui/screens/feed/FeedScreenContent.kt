package com.example.kloth.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.data.PostItem
import com.example.kloth.ui.screens.feed.components.PostCard

@Composable
fun FeedScreenContent(
    mockPosts: List<PostItem>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(mockPosts) { post ->
            PostCard(
                creatorName = post.creatorName,
                timeAgo = post.timeAgo,
                avatarUrl = post.avatarUrl,
                postImageUrl = post.postImageUrl,
                title = post.title,
                rating = post.rating,
                reviewCount = post.reviewCount,
                comments = post.comments,
                description = post.description,
                tags = post.tags,
                category = post.category,
                categoryColor = post.categoryColor,
                onClick = { onProductClick(post.id) }
            )
        }
    }
}
