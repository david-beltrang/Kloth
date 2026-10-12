package com.example.kloth.ui.screens.editProfile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun EditProfileTopBar(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val buttonBackgroundColor = if (isDarkTheme) Color.White else Color(0xFFE0E0E0)
    val buttonTextColor = Color.Black

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.edit_profile_cancel),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp,
                modifier = Modifier
                    .clickable(onClick = onCancelClick)
                    .padding(vertical = 4.dp),
            )
            Text(
                text = stringResource(R.string.edit_profile_title),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(buttonBackgroundColor)
                    .clickable(onClick = onSaveClick)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.edit_profile_save),
                    color = buttonTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileTopBarPreview() {
    KlothTheme {
        EditProfileTopBar()
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditProfileTopBarDarkPreview() {
    KlothTheme(darkTheme = true) {
        EditProfileTopBar()
    }
}
