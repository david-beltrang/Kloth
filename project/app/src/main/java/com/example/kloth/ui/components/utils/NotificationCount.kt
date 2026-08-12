package com.example.kloth.ui.components.utils

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.RedInferno

@Composable
fun NotificationCount(modifier: Modifier = Modifier, count: Int = 3, icon: ImageVector = Icons.Default.Notifications) {
    BadgedBox(
        badge = {
            if (count > 0) {
                Badge(
                    containerColor = RedInferno,
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(14.dp)
                        .offset(x = (0).dp, y = -2.dp) // Movemos el badge hacia adentro
                ) {
                    Text(
                        text = count.toString(),
                        fontSize = 9.sp
                    )
                }
            }
        }
    )
    {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(id = R.string.content_desc_notifications)
        )

    }

}

@Composable
@Preview(showBackground = true)
fun NotificationCountPreview(){
    NotificationCount()
}