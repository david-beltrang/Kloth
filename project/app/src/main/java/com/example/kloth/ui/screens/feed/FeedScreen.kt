package com.example.kloth.ui.screens.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.kloth.R
import com.example.kloth.navigation.Screen
import com.example.kloth.ui.theme.KlothSecondary
import com.example.kloth.ui.theme.RedInferno

data class PostItem(
    val id: String,
    val creatorName: String,
    val timeAgo: String,
    val avatarUrl: String,
    val postImageUrl: String,
    val title: String,
    val rating: String,
    val reviewCount: Int,
    val comments: String,
    val description: String,
    val tags: List<String>,
    val category: String,
    val categoryColor: Color
)

@Composable
fun FeedScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // datos de la entidad
    val mockPosts = FakeArticle.posts
    
    FeedScreenContent(modifier, mockPosts)
}
