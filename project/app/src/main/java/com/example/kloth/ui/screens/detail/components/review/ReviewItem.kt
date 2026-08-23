package com.example.kloth.ui.screens.detail.components.review

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.data.ReviewData
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun ReviewItem(
    review: ReviewData,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        ReviewHeader(
            authorName = review.authorName,
            timeAgo = review.timeAgo,
            avatarRes = review.avatarRes,
            rating = review.rating
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.reviewText,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        ReviewActions(
            likesCount = review.likesCount,
            commentsText = review.commentsText,
            onLikeClick = onLikeClick,
            onCommentClick = onCommentClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewItemPreview() {
    KlothTheme {
        ReviewItem(
            review = ReviewData(
                id = "1",
                authorName = "Elena V. Costanza",
                timeAgo = "Hace 2 días",
                avatarRes = R.drawable.abrigo_negro,
                rating = 5.0f,
                reviewText = "La calidad de la tela es excepcional. Es ligero pero increíblemente cálido. Definitivamente una inversión que vale cada céntimo para cualquier armario profesional.",
                likesCount = 24,
                commentsText = "2"
            )
        )
    }
}