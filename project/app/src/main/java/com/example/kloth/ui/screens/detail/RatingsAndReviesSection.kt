package com.example.kloth.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.kloth.R
import com.example.kloth.ui.components.ReviewItem
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun RatingsAndReviewsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Divider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.section_reviews),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reseña 1 - Elena V. Costanza
        ReviewItem(
            authorName = "Elena V. Costanza",
            timeAgo = "Hace 2 días",
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDZmZrqDC7H3x4f40zNWodV5qLVc4eHfqbFcT5OtfDUNQgmZWvV-qC-1tYe6GpeV0JvDnbYcIbz04xD5pgHL1S7a3HYGp6MnoNE8yAYyMHFtsKDbQsIQBL41bdWSAkHJKmO57-18dygYJ0yoY47QPQP-dl_Iycj_6n0UqfuLwR8oCLO7MuLPCHBHRmODjl4Sd9EvOdNWm2K-Nzue9Ia-ZPbM3VqnJO8gRjMerl7N7-HJc1odju_cuEmtw",
            rating = 5,
            reviewText = stringResource(R.string.mock_description)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Reseña 2 - Marcus Thorne
        ReviewItem(
            authorName = "Marcus Thorne",
            timeAgo = "Hace 1 semana",
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnwywEGxGQLCEBW4_GYEsXPlZE_XVWiCfO9zWOt_aKswJa6xiLXU_5tgLGGjPNy-lpw4u_sHM5z8_P9sAazAhgd6QPm3mUEWkq-BtdbObWlYfhliMGV2C_2YzxlAuCc2tHOZn2WiPrzOslT0LJHOEncUeTSLgmG4vkOYnwsYSmJCtagiqnK1olH28idmgfKMwqO6tNuZOJLMKUyUMYtsnRIYQ-PcoEbZ8yNcdjCwzQc24JyNBV71gPNQ",
            rating = 4,
            reviewText = stringResource(R.string.mock_description) // Usando string de recurso
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RatingsAndReviewsSectionPreview() {
    KlothTheme {
        RatingsAndReviewsSection()
    }
}
