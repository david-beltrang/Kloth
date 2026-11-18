package com.example.kloth.ui.screens.createArticle.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

@Composable
fun FormSelector(
    label: String,
    placeholder: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    required: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    FormSelectorField(
        label = label,
        placeholder = placeholder,
        options = options,
        selected = selected,
        onSelected = onSelected,
        required = required,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormSelectorField(
    label: String,
    placeholder: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    required: Boolean,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayedOptions = remember(options, selected) {
        if (selected.isNotBlank() && selected !in options) listOf(selected) + options else options
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (required) {
                Text(" *", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            TextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                placeholder = {
                    Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.heightIn(max = 300.dp).verticalScroll(rememberScrollState())
            ) {
                displayedOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Selector cerrado")
@Composable
private fun FormSelectorClosedPreview() {
    KlothTheme {
        val options = stringArrayResource(R.array.article_categories).toList()
        FormSelector(
            stringResource(R.string.label_categoria), stringResource(R.string.placeholder_categoria),
            options, "", {}, required = true, modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Selector desplegado")
@Composable
private fun FormSelectorExpandedPreview() {
    KlothTheme {
        val options = stringArrayResource(R.array.article_categories).toList()
        FormSelectorField(
            label = stringResource(R.string.label_categoria),
            placeholder = stringResource(R.string.placeholder_categoria),
            options = options,
            selected = options.first(),
            onSelected = {},
            required = true,
            expanded = true,
            onExpandedChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Selector con valor")
@Composable
private fun FormSelectorSelectedPreview() {
    KlothTheme {
        FormSelector(
            stringResource(R.string.label_estilo), stringResource(R.string.placeholder_estilo),
            stringArrayResource(R.array.outfit_styles).toList(),
            "Estilo legado", {}, modifier = Modifier.padding(16.dp)
        )
    }
}
