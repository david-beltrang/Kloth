package com.example.kloth.ui.screens.detail.components.review

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.data.ReviewData
import com.example.kloth.data.defaultReviewsList
import com.example.kloth.ui.screens.detail.components.RatingStars
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun RatingsAndReviewsSection(
    averageRating: Float = 4.9f,
    reviewsCountText: String = "(124 reseñas)",
    reviewsList: List<ReviewData> = defaultReviewsList,
    onWriteReviewClick: () -> Unit = {},
    onLikeReviewClick: (String) -> Unit = {},
    onCommentReviewClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Valoraciones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = averageRating.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    RatingStars(rating = averageRating, starSize = 14.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = reviewsCountText,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Button(
                onClick = onWriteReviewClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Escribir\nreseña",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        reviewsList.forEachIndexed { index, review ->
            ReviewItem(
                review = review,
                onLikeClick = { onLikeReviewClick(review.id) },
                onCommentClick = { onCommentReviewClick(review.id) }
            )

            if (index < reviewsList.size - 1) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RatingsAndReviewsSectionPreview() {
    KlothTheme {
        RatingsAndReviewsSection()
    }
}