package com.example.kloth.ui.screens.reviewsMap

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kloth.data.local.Coordinates
import com.example.kloth.data.local.ReviewMapPin
import com.example.kloth.ui.theme.KlothTheme

private val previewPins = listOf(
    ReviewMapPin(
        reviewId = "review_1",
        articleId = "article_1",
        authorId = "user_1",
        authorName = "Elena V. Costanza",
        avatarUrl = null,
        rating = 4.5f,
        reviewText = "Excelente chaqueta, la calidad del denim es muy buena.",
        timeAgo = "Hace 2 h",
        coordinates = Coordinates(4.7110, -74.0721)
    ),
    ReviewMapPin(
        reviewId = "review_2",
        articleId = "article_2",
        authorId = "user_2",
        authorName = "Alex Style",
        avatarUrl = null,
        rating = 5f,
        reviewText = "El mejor outfit que he comprado este año.",
        timeAgo = "Hace 5 h",
        coordinates = Coordinates(4.7150, -74.0680)
    )
)

@Preview(name = "Reviews Map - Light Mode", showSystemUi = true)
@Composable
fun ReviewsMapLightPreview() {
    KlothTheme(darkTheme = false) {
        ReviewsMapScreenContent(
            uiState = ReviewsMapState(pins = previewPins),
            onBackClick = {},
            onRetryClick = {},
            onRefreshClick = {},
            onPinSelected = {},
            onPinDismissed = {},
            onReviewClick = {},
            mapContent = { _, _ -> MapPlaceholder() }
        )
    }
}

@Preview(
    name = "Reviews Map - Dark Mode",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ReviewsMapDarkPreview() {
    KlothTheme(darkTheme = true) {
        ReviewsMapScreenContent(
            uiState = ReviewsMapState(pins = previewPins, selectedPin = previewPins.first()),
            onBackClick = {},
            onRetryClick = {},
            onRefreshClick = {},
            onPinSelected = {},
            onPinDismissed = {},
            onReviewClick = {},
            mapContent = { _, _ -> MapPlaceholder() }
        )
    }
}

@Preview(name = "Reviews Map - Empty", showSystemUi = true)
@Composable
fun ReviewsMapEmptyPreview() {
    KlothTheme {
        ReviewsMapScreenContent(
            uiState = ReviewsMapState(),
            onBackClick = {},
            onRetryClick = {},
            onRefreshClick = {},
            onPinSelected = {},
            onPinDismissed = {},
            onReviewClick = {},
            mapContent = { _, _ -> MapPlaceholder() }
        )
    }
}

@Composable
private fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Mapa",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
