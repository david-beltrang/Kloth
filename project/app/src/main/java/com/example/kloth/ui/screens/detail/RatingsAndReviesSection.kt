package com.example.kloth.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R

@Composable
fun RatingsAndReviewsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Divider(
            color = colorResource(id = R.color.outline_variant),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Valoraciones y Reseñas",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.on_surface)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}