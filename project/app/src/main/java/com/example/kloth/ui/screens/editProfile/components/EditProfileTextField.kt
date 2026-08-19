package com.example.kloth.ui.screens.editProfile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.ui.theme.LightGrayBackground
import com.example.kloth.ui.theme.TextGray

@Composable
fun EditProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true,
    height: Int? = null,
    showCharacterCount: Boolean = false,
    maxCharacters: Int = 150
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextGray,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        
        val textFieldModifier = if (height != null) {
            Modifier
                .fillMaxWidth()
                .height(height.dp)
        } else {
            Modifier.fillMaxWidth()
        }

        TextField(
            value = value,
            onValueChange = { newValue: String ->
                if (!showCharacterCount || newValue.length <= maxCharacters) {
                    onValueChange(newValue)
                }
            },
            placeholder = { 
                Text(text = placeholder, color = TextGray, fontSize = 14.sp) 
            },
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LightGrayBackground,
                unfocusedContainerColor = LightGrayBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = textFieldModifier,
            singleLine = singleLine
        )
        
        if (showCharacterCount) {
            Text(
                text = "${value.length}/$maxCharacters",
                fontSize = 12.sp,
                color = TextGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileTextFieldPreview() {
    EditProfileTextField(
        label = "Nombre de usuario",
        value = "alex_style",
        onValueChange = {},
        leadingIcon = Icons.Outlined.Search
    )
}
