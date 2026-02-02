package com.will.busnotification.util

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

/**
 * Utility to compute a user-friendly"time until" string from various arrival time text formats.
 * Returns examples like: "Agora", "1 min", "3 min", "2 h", "2 h 15 min" or falls back to the raw text.
 */
fun computeMinutesUntil(arrivalText: String): String {
    val trimmed = arrivalText.trim()
    if (trimmed.isEmpty()) return "---"

    // If the API already returned a relative minutes string, keep it
    val lower = trimmed.lowercase(Locale.getDefault())
    if (lower.contains("min") || lower.contains("minuto")) return trimmed
    if (lower.contains("agora") || lower.contains("now")) return "Agora"

    // Candidate time formats we expect from API
    val formatters = listOf(
        DateTimeFormatter.ofPattern("H:mm"),
        DateTimeFormatter.ofPattern("HH:mm"),
        DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("HH:mm:ss")
    )

    val nowZoned = ZonedDateTime.now(ZoneId.systemDefault())

    // Try parsing with each formatter as LocalTime
    for (fmt in formatters) {
        try {
            val parsedTime = LocalTime.parse(trimmed, fmt)
            // Combine with today's date; if parsed time is before now, assume next day
            var arrivalDateTime = LocalDateTime.of(LocalDate.now(), parsedTime)
            val nowLocal = LocalDateTime.ofInstant(nowZoned.toInstant(), ZoneId.systemDefault())
            if (arrivalDateTime.isBefore(nowLocal.minusMinutes(1))) {
                arrivalDateTime = arrivalDateTime.plusDays(1)
            }
            val minutes = Duration.between(nowLocal, arrivalDateTime).toMinutes()
            return when {
                minutes <= 0L -> "Agora"
                minutes == 1L -> "1 min"
                minutes < 60L -> "${minutes} min"
                else -> {
                    val hours = minutes / 60
                    val rem = minutes % 60
                    if (rem == 0L) "${hours} h" else "${hours} h ${rem} min"
                }
            }
        } catch (_: DateTimeParseException) {
            // try next formatter
        }
    }

    // If parsing failed, try ISO/ZonedDateTime parse
    try {
        val zdt = ZonedDateTime.parse(trimmed)
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        var target = zdt.withZoneSameInstant(ZoneId.systemDefault())
        if (target.isBefore(now)) target = target.plusDays(1)
        val minutes = Duration.between(now, target).toMinutes()
        return if (minutes <= 0L) "Agora" else "${minutes} min"
    } catch (_: Exception) {
        // ignore
    }

    // Last resort: extract first number and treat it as minutes
    val digits = Regex("(\\d+)").find(trimmed)?.value
    if (digits != null) {
        val v = digits.toLongOrNull() ?: return trimmed
        return if (v <= 0) "Agora" else if (v == 1L) "1 min" else "${v} min"
    }

    return trimmed
}
