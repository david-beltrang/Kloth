package com.example.kloth.ui.screens.createArticle.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.R
import com.example.kloth.ui.theme.KlothTheme

enum class ArticleType {
    PRENDA, OUTFIT, EVENTO
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormArticle(
    selectedType: ArticleType,
    nombre: String,
    onNombreChange: (String) -> Unit,
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    marca: String,
    onMarcaChange: (String) -> Unit,
    color: String,
    onColorChange: (String) -> Unit,
    precio: String,
    onPrecioChange: (String) -> Unit,
    estilo: String,
    onEstiloChange: (String) -> Unit,
    ciudad: String,
    onCiudadChange: (String) -> Unit,
    pais: String,
    onPaisChange: (String) -> Unit,
    organizador: String,
    onOrganizadorChange: (String) -> Unit,
    tags: List<String>,
    tagInput: String,
    onTagInputChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
    onPublicarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        FormInput(
            label = stringResource(R.string.label_nombre),
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = stringResource(R.string.placeholder_nombre),
            required = true
        )

        FormInput(
            label = stringResource(R.string.label_descripcion),
            value = descripcion,
            onValueChange = onDescripcionChange,
            placeholder = stringResource(R.string.placeholder_descripcion),
            singleLine = false,
            minLines = 4,
            maxLines = 6,
            maxLength = 500
        )

        AnimatedContent(targetState = selectedType, label = "form_transition") { type ->
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                when (type) {
                    ArticleType.PRENDA -> {
                        FormInput(
                            label = stringResource(R.string.label_marca),
                            value = marca,
                            onValueChange = onMarcaChange,
                            placeholder = stringResource(R.string.placeholder_marca)
                        )
                        FormInput(
                            label = stringResource(R.string.label_categoria),
                            value = "",
                            onValueChange = {},
                            placeholder = stringResource(R.string.placeholder_categoria),
                            required = true,
                            isSelector = true
                        )
                        FormInput(
                            label = stringResource(R.string.label_color_field),
                            value = color,
                            onValueChange = onColorChange,
                            placeholder = stringResource(R.string.placeholder_color)
                        )
                        FormInput(
                            label = stringResource(R.string.label_precio),
                            value = precio,
                            onValueChange = onPrecioChange,
                            placeholder = stringResource(R.string.placeholder_precio),
                            prefix = stringResource(R.string.currency_symbol),
                            keyboardType = KeyboardType.Number
                        )
                    }
                    ArticleType.OUTFIT -> {
                        FormInput(
                            label = stringResource(R.string.label_estilo),
                            value = estilo,
                            onValueChange = onEstiloChange,
                            placeholder = stringResource(R.string.placeholder_estilo),
                            isSelector = true
                        )
                    }
                    ArticleType.EVENTO -> {
                        FormInput(
                            label = stringResource(R.string.label_ciudad),
                            value = ciudad,
                            onValueChange = onCiudadChange,
                            placeholder = stringResource(R.string.placeholder_ciudad)
                        )
                        FormInput(
                            label = stringResource(R.string.label_pais),
                            value = pais,
                            onValueChange = onPaisChange,
                            placeholder = stringResource(R.string.placeholder_pais)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                FormInput(
                                    label = stringResource(R.string.label_inicio),
                                    value = "",
                                    onValueChange = {},
                                    placeholder = stringResource(R.string.placeholder_fecha),
                                    isSelector = true
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                FormInput(
                                    label = stringResource(R.string.label_fin),
                                    value = "",
                                    onValueChange = {},
                                    placeholder = stringResource(R.string.placeholder_fecha),
                                    isSelector = true
                                )
                            }
                        }
                        FormInput(
                            label = stringResource(R.string.label_organizador),
                            value = organizador,
                            onValueChange = onOrganizadorChange,
                            placeholder = stringResource(R.string.placeholder_organizador)
                        )
                    }
                }
            }
        }

        TagsSection(
            tags = tags,
            tagInput = tagInput,
            onTagInputChange = onTagInputChange,
            onAddTag = onAddTag,
            onRemoveTag = onRemoveTag
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onPublicarClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.btn_publicar),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun FormInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    maxLength: Int? = null,
    isSelector: Boolean = false,
    prefix: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            if (required) {
                Text(
                    text = " *",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        TextField(
            value = value,
            onValueChange = { if (maxLength == null || it.length <= maxLength) onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            prefix = prefix?.let { { Text(text = "$it ") } },
            trailingIcon = if (isSelector) {
                { Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null) }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (maxLength != null) {
            Text(
                text = "${value.length}/$maxLength",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}
@Composable
private fun TagsSection(
    tags: List<String>,
    tagInput: String,
    onTagInputChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.label_tags),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = tagInput,
                onValueChange = onTagInputChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = stringResource(R.string.placeholder_tags),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(12.dp)
            )
            IconButton(onClick = onAddTag) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_add_tag),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (tags.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    FilterChip(
                        selected = false,
                        onClick = { onRemoveTag(tag) },
                        label = { Text(text = "#$tag") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Form - Prenda")
@Composable
fun FormArticlePrendaPreview() {
    KlothTheme {
        FormArticle(
            selectedType = ArticleType.PRENDA,
            nombre = "", onNombreChange = {},
            descripcion = "", onDescripcionChange = {},
            marca = "", onMarcaChange = {},
            color = "", onColorChange = {},
            precio = "", onPrecioChange = {},
            estilo = "", onEstiloChange = {},
            ciudad = "", onCiudadChange = {},
            pais = "", onPaisChange = {},
            organizador = "", onOrganizadorChange = {},
            tags = emptyList(), tagInput = "",
            onTagInputChange = {}, onAddTag = {}, onRemoveTag = {},
            onPublicarClick = {},
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}

@Preview(showBackground = true, name = "Form - Outfit")
@Composable
fun FormArticleOutfitPreview() {
    KlothTheme {
        FormArticle(
            selectedType = ArticleType.OUTFIT,
            nombre = "", onNombreChange = {},
            descripcion = "", onDescripcionChange = {},
            marca = "", onMarcaChange = {},
            color = "", onColorChange = {},
            precio = "", onPrecioChange = {},
            estilo = "", onEstiloChange = {},
            ciudad = "", onCiudadChange = {},
            pais = "", onPaisChange = {},
            organizador = "", onOrganizadorChange = {},
            tags = listOf("streetwear", "casual"), tagInput = "",
            onTagInputChange = {}, onAddTag = {}, onRemoveTag = {},
            onPublicarClick = {},
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}

@Preview(showBackground = true, name = "Form - Evento")
@Composable
fun FormArticleEventoPreview() {
    KlothTheme {
        FormArticle(
            selectedType = ArticleType.EVENTO,
            nombre = "", onNombreChange = {},
            descripcion = "", onDescripcionChange = {},
            marca = "", onMarcaChange = {},
            color = "", onColorChange = {},
            precio = "", onPrecioChange = {},
            estilo = "", onEstiloChange = {},
            ciudad = "", onCiudadChange = {},
            pais = "", onPaisChange = {},
            organizador = "", onOrganizadorChange = {},
            tags = emptyList(), tagInput = "",
            onTagInputChange = {}, onAddTag = {}, onRemoveTag = {},
            onPublicarClick = {},
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}