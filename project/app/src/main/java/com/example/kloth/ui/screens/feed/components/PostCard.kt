package com.example.kloth.ui.screens.feed.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kloth.R
import com.example.kloth.data.FakeArticle
import com.example.kloth.data.PostItem
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.theme.RedInferno
import com.example.kloth.ui.theme.GoldStar

@Composable
fun PostCard(
    post: PostItem,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    ) {
        Column {
            PostHeader(
                name = post.creatorName,
                time = post.timeAgo,
                avatarUrl = post.avatarUrl,
                modifier = Modifier.padding(16.dp)
            )

            PostContent(
                imageModel = post.product.imageUrl ?: post.product.imageRes,
                category = post.product.categoryTag,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
            )

            PostFooter(
                title = post.product.title,
                rating = post.product.averageRating,
                reviewCount = post.product.reviewsCountText,
                comments = post.commentsCount,
                description = post.product.description,
                tags = post.tags,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun PostHeader(
    name: String,
    time: String,
    avatarUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            //Se usa async para que mno se trabe la UI
            AsyncImage(
                model = avatarUrl,
                contentDescription = stringResource(R.string.content_desc_avatar),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PostContent(
    imageModel: Any?,
    category: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        //Se usa async para que mno se trabe la UI, carga asíncrona
        AsyncImage(
            model = imageModel,
            contentDescription = stringResource(R.string.content_desc_post_image),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        KlothBadge(
            text = category,
            containerColor = RedInferno,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
    }
}


@Composable
private fun PostFooter(
    title: String,
    rating: Float,
    reviewCount: String,
    comments: String,
    description: String,
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldStar,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rating.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reviewCount,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                SocialAction(
                    icon = Icons.Default.ChatBubbleOutline,
                    count = comments,
                    contentDescription = stringResource(R.string.content_desc_comment)
                )
            }
        }

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )

        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tags) { tag ->
                KlothBadge(
                    text = stringResource(R.string.label_tag_prefix, tag),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun SocialAction(
    icon: ImageVector,
    count: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(24.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = count,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PostCardPreview() {
    KlothTheme {
        PostCard(
            post = FakeArticle.posts.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
