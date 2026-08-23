package com.example.kloth.ui.screens.detail.components.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun ReviewActions(
    likesCount: Int,
    commentsText: String,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onLikeClick() }
        ) {
            Icon(
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = "Likes",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )
            if (likesCount > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = likesCount.toString(),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onCommentClick() }
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Comentarios",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )
            if (commentsText.isNotEmpty()) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = commentsText,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewActionsPreview() {
    KlothTheme {
        ReviewActions(
            likesCount = 24,
            commentsText = "2"
        )
    }
}