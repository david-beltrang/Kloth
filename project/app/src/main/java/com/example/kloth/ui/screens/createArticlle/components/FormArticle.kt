package com.example.kloth.ui.screens.createArticlle.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kloth.ui.theme.KlothTheme

/*
  Estos son los 3 tipos de publicaciones que nuestra app permite crear.
 */
enum class ArticleType {
    PRENDA, OUTFIT, EVENTO
}

/**
 * Este es el formulario principal. Es STATELESS (no tiene memoria interna).
 * Recibe todos los valores y las funciones para avisar cuando cambian.
 */
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
    onPublicarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // campos que siempre van
        FormInput(
            label = "Nombre",
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = "Nombre del artículo",
            required = true
        )

        FormInput(
            label = "Descripción",
            value = descripcion,
            onValueChange = onDescripcionChange,
            placeholder = "Describe este artículo...",
            singleLine = false,
            minLines = 4,
            maxLines = 6,
            maxLength = 500
        )

        // campos que cambian segun la categoria
        AnimatedContent(targetState = selectedType, label = "form_transition") { type ->
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                when (type) {
                    ArticleType.PRENDA -> {
                        FormInput(label = "Marca", value = marca, onValueChange = onMarcaChange, placeholder = "Marca o diseñador")
                        FormInput(label = "Categoría", value = "", onValueChange = {}, placeholder = "Seleccionar categoría", required = true, isSelector = true)
                        FormInput(label = "Color", value = color, onValueChange = onColorChange, placeholder = "Color principal")
                        FormInput(label = "Precio", value = precio, onValueChange = onPrecioChange, placeholder = "0.00", prefix = "€", keyboardType = KeyboardType.Number)
                    }
                    ArticleType.OUTFIT -> {
                        FormInput(label = "Estilo", value = estilo, onValueChange = onEstiloChange, placeholder = "Seleccionar estilo", isSelector = true)
                    }
                    ArticleType.EVENTO -> {
                        FormInput(label = "Ciudad", value = ciudad, onValueChange = onCiudadChange, placeholder = "Ciudad del evento")
                        FormInput(label = "País", value = pais, onValueChange = onPaisChange, placeholder = "País del evento")
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                FormInput(label = "Inicio", value = "", onValueChange = {}, placeholder = "DD/MM/AA", isSelector = true)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                FormInput(label = "Fin", value = "", onValueChange = {}, placeholder = "DD/MM/AA", isSelector = true)
                            }
                        }
                        FormInput(label = "Organizador", value = organizador, onValueChange = onOrganizadorChange, placeholder = "Nombre del organizador")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón final
        Button(
            onClick = onPublicarClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Publicar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

/*
Componente visual para las entradas de texto.
 */
@Composable
fun FormInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    required: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    maxLength: Int? = null,
    isSelector: Boolean = false,
    prefix: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            if (required) {
                Text(text = " *", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
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

// Previews con datos estáticos para visualización

@Preview(showBackground = true)
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
            onPublicarClick = {},
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}
