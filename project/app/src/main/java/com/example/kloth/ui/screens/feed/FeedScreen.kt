package com.example.kloth.ui.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.kloth.data.FakeArticle
import com.example.kloth.ui.screens.feed.components.FeedTabRow

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val mockPosts = FakeArticle.posts
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FeedTabRow(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
            FeedScreenContent(
                mockPosts = mockPosts,
                onProductClick = onProductClick
            )
        }
    }
}
