package com.example.kloth.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.data.local.PostItem
import com.example.kloth.ui.components.ErrorState
import com.example.kloth.ui.screens.feed.components.PostCard

@Composable
fun FeedScreenContent(
    // Variables de estado (Datos que vienen del ViewModel)
    posts: List<PostItem>,
    isLoading: Boolean,
    errorMessage: String?,

    // Eventos y Navegación
    onRetry: () -> Unit,
    onProductClick: (String) -> Unit,

    modifier: Modifier = Modifier
) {
    when {
        isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage != null -> {
            ErrorState(
                message = errorMessage,
                onRetry = onRetry,
                modifier = modifier.fillMaxSize()
            )
        }

        posts.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.state_empty_posts),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(posts) { post ->
                    PostCard(
                        post = post,
                        onClick = { onProductClick(post.product.id) }
                    )
                }
            }
        }
    }
}
