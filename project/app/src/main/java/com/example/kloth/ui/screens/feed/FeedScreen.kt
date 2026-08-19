package com.example.kloth.ui.screens.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kloth.data.FakeArticle

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier
) {
    // datos de la entidad
    val mockPosts = FakeArticle.posts
    
    FeedScreenContent(
        modifier = modifier,
        mockPosts = mockPosts,
        onProductClick = { }
    )
}
