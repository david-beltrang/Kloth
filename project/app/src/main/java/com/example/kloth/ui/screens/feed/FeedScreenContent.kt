package com.example.kloth.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kloth.data.PostItem
import com.example.kloth.ui.utils.KlothBottomNavigation
import com.example.kloth.ui.utils.KlothTopAppBar
import com.example.kloth.ui.screens.feed.components.FeedTabRow
import com.example.kloth.ui.screens.feed.components.PostCard

@Composable
fun FeedScreenContent(modifier: Modifier = Modifier, mockPosts: List<PostItem>) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                KlothTopAppBar()
                FeedTabRow()
            }
        },
        bottomBar = {
            KlothBottomNavigation(selectedItem = 0)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                    categoryColor = post.categoryColor
                )
            }

            // Extra spacer for the bottom navigation
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
