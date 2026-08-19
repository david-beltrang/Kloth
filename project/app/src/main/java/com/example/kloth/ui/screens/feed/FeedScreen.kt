package com.example.kloth.ui.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kloth.data.FakeArticle
import com.example.kloth.ui.screens.feed.components.FeedTabRow

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier
) {
    // datos de la entidad
    val mockPosts = FakeArticle.posts
    
    Column(modifier = modifier) {
        FeedTabRow()
        FeedScreenContent(
            mockPosts = mockPosts,
            onProductClick = { }
        )
    }
}
