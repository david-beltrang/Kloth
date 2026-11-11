package com.example.kloth.ui.screens.profile

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kloth.data.local.PostItem
import com.example.kloth.data.local.ModelPicType
import com.example.kloth.ui.screens.modelPics.ModelPicViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onDeleteArticleClick: (PostItem) -> Unit = {},
    onEditArticleClick: (String) -> Unit = {},
    profileUserId: String? = null,
    onFollowListClick: (String, String) -> Unit = { _, _ -> },
    initialTabIndex: Int = 0,
    initialModelPicType: ModelPicType = ModelPicType.BASE,
    highlightedModelPicId: String? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val modelPicViewModel: ModelPicViewModel = hiltViewModel()
    val modelPicState by modelPicViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            modelPicViewModel.uploadBaseModelPic(
                imageUri = uri,
                name = getImageDisplayName(context, uri),
                contentType = context.contentResolver.getType(uri)
            )
        }
    }

    // Se recarga cada vez que la pantalla vuelve a entrar en composición
    LaunchedEffect(Unit) {
        viewModel.refreshProfile(profileUserId)
        if (initialTabIndex != 0) viewModel.onTabSelected(initialTabIndex)
        modelPicViewModel.selectFilter(initialModelPicType)
    }

    LaunchedEffect(uiState.selectedTabIndex, uiState.viewingOwnProfile) {
        if (uiState.selectedTabIndex == 2 && uiState.viewingOwnProfile) {
            modelPicViewModel.loadModelPics()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProfileScreenContent(
            email = uiState.email,
            username = uiState.username,
            bio = uiState.bio,
            location = uiState.location,
            website = uiState.website,
            profileImageUrl = uiState.profileImageUrl,
            viewingOwnProfile = uiState.viewingOwnProfile,
            followersCount = uiState.followersCount,
            followingCount = uiState.followingCount,
            isFollowing = uiState.isFollowing,
            isFollowActionInProgress = uiState.isFollowActionInProgress,
            selectedTabIndex = uiState.selectedTabIndex,
            userArticles = uiState.userArticles,
            reviewedArticles = uiState.reviewedArticles,
            modelPicState = modelPicState,
            highlightedModelPicId = highlightedModelPicId,
            onTabSelected = { viewModel.onTabSelected(it) },
            onModelPicFilterSelected = modelPicViewModel::selectFilter,
            onUploadModelPicClick = { imagePicker.launch("image/*") },
            onEditProfileClick = onEditProfileClick,
            onFollowClick = { viewModel.toggleFollow() },
            onLogoutClick = { viewModel.logOutButtonPressed() },
            onFollowersClick = { uiState.targetUserId?.let { onFollowListClick(it, "followers") } },
            onFollowingClick = { uiState.targetUserId?.let { onFollowListClick(it, "following") } },
            onProductClick = onProductClick,
            onDeleteArticleClick = onDeleteArticleClick,
            onEditArticleClick = onEditArticleClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun getImageDisplayName(context: Context, uri: Uri): String {
    val displayName = runCatching {
        context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            val nameColumn = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameColumn >= 0 && cursor.moveToFirst()) cursor.getString(nameColumn) else null
        }
    }.getOrNull()

    return displayName
        ?.substringBeforeLast('.', displayName)
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?: "Foto de modelo"
}
