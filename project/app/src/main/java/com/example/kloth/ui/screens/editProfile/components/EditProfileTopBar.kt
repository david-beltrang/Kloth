package com.example.kloth.ui.screens.editProfile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.DividerColor
import com.example.kloth.ui.theme.TextGray

@Composable
fun EditProfileTopBar(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val logoResource = if (isSystemInDarkTheme()) {
        R.drawable.logo_blanco
    } else {
        R.drawable.logo_negro
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = logoResource),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.size(height = 30.dp, width = 130.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.edit_profile_cancel),
                color = TextGray,
                fontSize = 15.sp,
                modifier = Modifier.clickable { onCancel() }
            )
            Text(
                text = stringResource(R.string.edit_profile_title),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = stringResource(R.string.edit_profile_save),
                color = TextGray,
                fontSize = 15.sp,
                modifier = Modifier.clickable { onSave() }
            )
        }
        HorizontalDivider(thickness = 1.dp, color = DividerColor)
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileTopBarPreview() {
    EditProfileTopBar()
}
