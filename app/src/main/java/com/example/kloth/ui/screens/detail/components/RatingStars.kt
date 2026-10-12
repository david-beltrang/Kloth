package com.example.kloth.ui.screens.detail.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kloth.ui.theme.GoldStar
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun RatingStars(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Dp = 14.dp,
    starColor: Color = GoldStar,
    onRatingSelected: ((Int) -> Unit)? = null
) {
    val unselectedColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)

    Row(modifier = modifier) {
        for (i in 1..5) {
            val icon = when {
                rating >= i -> Icons.Default.Star
                rating >= i - 0.5f -> Icons.Default.StarHalf
                else -> Icons.Outlined.StarBorder
            }
            val tint = if (rating >= i - 0.5f) starColor else unselectedColor

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .size(starSize)
                    .padding(end = 4.dp)
                    .then(
                        if (onRatingSelected != null) {
                            Modifier.clickable { onRatingSelected(i) }
                        } else Modifier
                    )
            )
        }
    }
}

@Preview(name = "RatingStars - ReadOnly Light", showBackground = true)
@Composable
fun RatingStarsReadOnlyPreview() {
    KlothTheme {
        RatingStars(rating = 4.5f)
    }
}

@Preview(name = "RatingStars - Interactive Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RatingStarsInteractivePreview() {
    KlothTheme(darkTheme = true) {
        RatingStars(
            rating = 3f,
            starSize = 32.dp,
            onRatingSelected = {}
        )
    }
}