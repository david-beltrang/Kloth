package com.example.kloth.ui.screens.followList

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.data.local.UserProfileInfo
import com.example.kloth.ui.theme.KlothTheme

private val previewUsers = listOf(
    UserProfileInfo("1", "ana_lopez", "ana@example.com", null, null, null, null),
    UserProfileInfo("2", "carlos_ruiz", "carlos@example.com", null, null, null, null)
)

@Preview(name = "Follow list - Light", showBackground = true, showSystemUi = true)
@Composable
fun FollowListScreenPreview() {
    KlothTheme(darkTheme = false) {
        FollowListScreenContent(
            users = previewUsers,
            isLoading = false,
            listType = "following",
            onBackClick = {},
            onUserClick = {},
            onUnfollowClick = {}
        )
    }
}

@Preview(name = "Follow list - Dark", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FollowListScreenDarkPreview() {
    KlothTheme(darkTheme = true) {
        FollowListScreenContent(
            users = previewUsers,
            isLoading = false,
            listType = "followers",
            onBackClick = {},
            onUserClick = {},
            onUnfollowClick = {}
        )
    }
}
