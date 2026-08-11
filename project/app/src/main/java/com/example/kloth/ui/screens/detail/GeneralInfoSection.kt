package com.example.kloth.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R

@Composable
fun GeneralInfoSection(
    productId: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(colorResource(id = R.color.sunset))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "PRENDAS",
                color = colorResource(id = R.color.on_surface),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Abrigo de Cachemira",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.on_surface)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$280.000 COP",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Abrigo corte clásico confeccionado en mezcla de lana y cachemira. Diseñado para ofrecer máxima calidez y elegancia durante temporadas frías.",
            fontSize = 14.sp,
            color = colorResource(id = R.color.on_surface_variant),
            lineHeight = 20.sp
        )
    }
}