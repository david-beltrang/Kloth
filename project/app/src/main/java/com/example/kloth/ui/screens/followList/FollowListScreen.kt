package com.example.kloth.ui.screens.followList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FollowListScreen(
    userId: String,
    listType: String,
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    viewModel: FollowListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(userId, listType) { viewModel.load(userId, listType) }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        FollowListScreenContent(
            users = state.users,
            isLoading = state.isLoading,
            listType = listType,
            onBackClick = onBackClick,
            onUserClick = onUserClick,
            onUnfollowClick = viewModel::unfollow,
            modifier = Modifier.fillMaxSize()
        )
    }
}
