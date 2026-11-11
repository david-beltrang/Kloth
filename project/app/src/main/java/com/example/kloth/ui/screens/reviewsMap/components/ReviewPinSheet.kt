package com.example.kloth.ui.screens.reviewsMap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.data.local.ReviewMapPin
import com.example.kloth.ui.screens.detail.components.review.ReviewHeader

// Contenido del bottom sheet que aparece al tocar un marcador del mapa.
@Composable
fun ReviewPinSheet(
    pin: ReviewMapPin,
    onOpenReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewHeader(
            authorName = pin.authorName,
            timeAgo = pin.timeAgo,
            avatarRes = R.drawable.profile,
            avatarUrl = pin.avatarUrl,
            rating = pin.rating
        )

        Text(
            text = pin.reviewText,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis
        )

        Button(
            onClick = onOpenReview,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.review_pin_open))
        }
    }
}
