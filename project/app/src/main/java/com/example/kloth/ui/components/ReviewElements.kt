package com.example.kloth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kloth.ui.theme.*

@Composable
fun RatingStars(rating: Float) {
    Row {
        for (i in 1..5) {
            val icon = when {
                rating >= i -> Icons.Default.Star
                rating >= i - 0.5f -> Icons.Default.StarHalf
                else -> Icons.Outlined.Star
            }
            val tint = if (rating >= i - 0.5f) PrimaryColor else OutlineVariant
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ReviewItem(
    authorName: String,
    timeAgo: String,
    avatarUrl: String,
    rating: Int,
    reviewText: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = authorName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(SurfaceContainer)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = authorName, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = OnSurfaceColor)
                    Text(text = timeAgo, fontSize = 12.sp, color = OnSurfaceVariantColor)
                }
            }
            RatingStars(rating = rating.toFloat())
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = reviewText, fontSize = 14.sp, color = OnSurfaceVariantColor, lineHeight = 20.sp)
    }
}