package com.example.kloth.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kloth.R
import com.example.kloth.ui.screens.feed.components.FeedTabRow

@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {}
) {
    val state by feedViewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FeedTabRow(
                selectedTabIndex = state.selectedTabIndex,
                onTabSelected = { feedViewModel.onTabSelected(it) }
            )
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.errorMessage ?: "",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        TextButton(onClick = { feedViewModel.getAllPosts() }) {
                            Text(text = stringResource(R.string.feed_retry))
                        }
                    }
                }
                else -> FeedScreenContent(
                    mockPosts = state.posts,
                    onProductClick = onProductClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
