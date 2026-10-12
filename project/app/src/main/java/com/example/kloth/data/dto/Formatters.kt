package com.example.kloth.data.dto

import java.text.NumberFormat
import java.time.Instant
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Precio en formato europeo: 549900.0 -> "549.900€".
 * Solo lo usan los artículos cuyo tipo tiene atributo de precio (PRENDA).
 */
fun Double.toPriceLabel(): String {
    val format = NumberFormat.getIntegerInstance(Locale("es", "ES"))
    return "${format.format(this.toLong())}€"
}

/**
 * Convierte un timestamp ISO 8601 del backend en un texto relativo:
 * "2 horas", "1 día", "3 semanas", "Hace 2 días".
 *
 * @param prefix texto previo cuando ya hay una diferencia medible ("Hace ").
 */
fun String.toTimeAgo(prefix: String = ""): String {
    val epochMillis = runCatching { Instant.parse(this).toEpochMilli() }
        .getOrNull() ?: return this

    val diff = System.currentTimeMillis() - epochMillis
    if (diff <= 0) return "ahora"

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    val amount: Long
    val unit: String

    when {
        minutes < 1 -> return if (prefix.isEmpty()) "ahora" else "Ahora"
        hours < 1 -> {
            amount = minutes
            unit = if (minutes == 1L) "minuto" else "minutos"
        }
        days < 1 -> {
            amount = hours
            unit = if (hours == 1L) "hora" else "horas"
        }
        days < 7 -> {
            amount = days
            unit = if (days == 1L) "día" else "días"
        }
        days < 30 -> {
            amount = days / 7
            unit = if (amount == 1L) "semana" else "semanas"
        }
        days < 365 -> {
            amount = days / 30
            unit = if (amount == 1L) "mes" else "meses"
        }
        else -> {
            amount = days / 365
            unit = if (amount == 1L) "año" else "años"
        }
    }

    return "$prefix$amount $unit"
}
