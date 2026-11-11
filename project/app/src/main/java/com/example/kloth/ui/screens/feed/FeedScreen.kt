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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kloth.R
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.ui.screens.feed.components.FeedTabRow
import com.example.kloth.ui.screens.tryOn.TryOnViewModel
import com.example.kloth.ui.screens.tryOn.components.TryOnDialog

@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {},
    onProfileClick: (String) -> Unit = {},
    onNavigateToModelPics: (ModelPicType, String?) -> Unit = { _, _ -> }
) {
    val state by feedViewModel.uiState.collectAsState()
    val tryOnViewModel: TryOnViewModel = hiltViewModel()
    val tryOnState by tryOnViewModel.uiState.collectAsState()

    // Se recarga cada vez que la pantalla vuelve a entrar en composición
    LaunchedEffect(Unit) {
        feedViewModel.getAllPosts()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("homeScreen")
                .then(if (tryOnState.isDialogVisible) Modifier.blur(10.dp) else Modifier),
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
                    state.selectedTabIndex == 0 && !state.hasFollowing -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.feed_follow_prompt),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                    else -> FeedScreenContent(
                        mockPosts = state.posts,
                        onProductClick = onProductClick,
                        onProfileClick = onProfileClick,
                        onTryOnClick = { post ->
                            tryOnViewModel.openDialog(post.product.id, post.product.imageUrl)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                state.selectedTabIndex == 0 && !state.hasFollowing -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.feed_follow_prompt),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
                else -> FeedScreenContent(
                    mockPosts = state.posts,
                    onProductClick = onProductClick,
                    onProfileClick = onProfileClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        TryOnDialog(
            state = tryOnState,
            onDismiss = tryOnViewModel::dismissDialog,
            onSelectBaseModelPic = tryOnViewModel::selectBaseModelPic,
            onRetryClick = tryOnViewModel::retryLoadModelPics,
            onGenerateClick = tryOnViewModel::requestGeneration,
            onUploadBasePicClick = {
                tryOnViewModel.dismissDialog()
                onNavigateToModelPics(ModelPicType.BASE, null)
            },
            onContinueClick = { modelPicId ->
                tryOnViewModel.onContinueConsumed()
                onNavigateToModelPics(ModelPicType.GENERATED, modelPicId)
            }
        )
    }
}
