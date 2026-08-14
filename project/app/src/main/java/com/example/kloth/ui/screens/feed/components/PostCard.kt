package com.example.kloth.ui.screens.feed.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.kloth.ui.theme.KlothTheme
import com.example.kloth.ui.theme.RedInferno
import com.example.kloth.ui.theme.StarYellow
import com.example.kloth.ui.theme.Sunset

@Composable
fun PostCard(
    creatorName: String, //Nombre del usuarui
    timeAgo: String, //Hace cuanto se publicó el artículo
    avatarUrl: String, // URL de la foto de perfil
    postImageUrl: String, // URL de la imagen del post
    title: String, //Titulo de la publicacion
    rating: String, //Calificacion de la publicacion
    reviewCount: Int, //Cantidad de reseñas
    comments: String, //Cantidad de comentarios
    description: String, //Descripcion de la pumlicacion
    tags: List<String>, //Lista de tags
    category: String, //Categoria o tipo del artículo
    categoryColor: Color, //Color de la etiqueta de la categoria del articulo
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(10.dp),
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
            // Creator Header
            PostHeader(
                name = creatorName,
                time = timeAgo,
                avatarUrl = avatarUrl,
                modifier = Modifier.padding(16.dp)
            )

            // Content (Image)
            PostContent(
                imageUrl = postImageUrl,
                category = category,
                categoryColor = categoryColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
            )

            // Footer Content
            PostFooter(
                title = title,
                rating = rating,
                reviewCount = reviewCount,
                comments = comments,
                description = description,
                tags = tags,
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
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
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
    imageUrl: String,
    category: String,
    categoryColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Post Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        KlothBadge(
            text = category,
            containerColor = categoryColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
    }
}


@Composable
private fun PostFooter(
    title: String,
    rating: String,
    reviewCount: Int,
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
                        tint = StarYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rating,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.label_reviews, reviewCount),
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
                    text = "#$tag",
                    containerColor = Sunset,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
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
            creatorName = "Elias Thorne",
            timeAgo = "2 hours ago",
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBMyCAVr9pWrd_sDsyfrx2v7B_wwZXc4En_Hj06fTPAnAoiyn85C9FsH8p6U6PiT3-zoF2ygVqjkcaxTd2qvb0dJco59T-gId4rhTYZRrypPBw7D21cyI8jziwbOHXhi6RvASwrKdMXNtFM4PZ4vmw0j-UQYyz-l9jQO_PKjkApgQurneLQYUD05xXGaJiFbJ7J7lnhsLCcMpV1reEReT6SL0W_OBqa0a_7S4P7IwHGSYytO0DdrGagg",
            postImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDpW3hTQoK-jwp_QytcmHQOJubOsBnRcVsFRLEcNgPdJCKBG-Az5EM7-DRqTigUiaQ6z8HyWpQaHZUwssvqN8KO_vvC8raEh0FUbOpUv2AXfPTNpcyQktFe7wSFGEyhbhuVrHh0loG-el0Ziv7BFu3YgwsjMTEzkEsjjHKuEE4VW43CRnM6mepoinTZokHM9SxVv_FfSXm_48q454nLpntqlr6ieAxDR6VBbjzF9cH3h3UimDgRUF9Tng",
            title = "Sculpted Obsidian Ensemble",
            rating = "4.8",
            reviewCount = 124,
            comments = "42",
            description = stringResource(R.string.mock_description),
            tags = listOf("minimalist", "fall24", "obsidian"),
            category = stringResource(R.string.badge_couture),
            categoryColor = RedInferno,
            modifier = Modifier.padding(16.dp)
        )
    }
}
