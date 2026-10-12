package com.example.kloth.ui.screens.detail.components.review

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.screens.detail.components.RatingStars
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.utils.AvatarImage

@Composable
fun ReviewHeader(
    authorName: String,
    timeAgo: String,
    avatarUrl: String?,
    @DrawableRes avatarRes: Int?,
    rating: Float,
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onProfileClick() }
        ) {
            AvatarImage(
                url = avatarUrl,
                fallbackRes = avatarRes,
                contentDescription = authorName,
                size = 36
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = authorName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = timeAgo,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        RatingStars(rating = rating, starSize = 12.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewHeaderPreview() {
    KlothTheme {
        ReviewHeader(
            authorName = "Elena V. Costanza",
            timeAgo = "Hace 2 días",
            avatarUrl = null,
            avatarRes = R.drawable.abrigo_negro,
            rating = 5.0f
        )
    }
}