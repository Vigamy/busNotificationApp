package com.will.busnotification.notification

import android.util.Log
import com.will.busnotification.BuildConfig
import com.will.busnotification.data.dto.AdressRequest
import com.will.busnotification.data.dto.RouteRequest
import com.will.busnotification.data.dto.TransitPreferences
import com.will.busnotification.data.network.GooglePlacesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

/**
 * Resultado de checagem de um ônibus individual.
 */
data class BusArrivalResult(
    val lineCode: String,
    val lineName: String,
    val departureStop: String,
    val arrivalStop: String,
    val minutesUntilArrival: Long,
    val hasPassedStop: Boolean
)

/**
 * Checa a ETA dos ônibus salvos consultando a Google Routes API.
 */
object BusArrivalChecker {

    private const val TAG = "BusArrivalChecker"

    private val apiService: GooglePlacesApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://routes.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GooglePlacesApiService::class.java)
    }

    /**
     * Consulta a Routes API pra uma linha específica e retorna o ETA.
     *
     * @param departureStop endereço/nome do ponto de embarque
     * @param destination destino da linha (headsign)
     * @param lineCode código da linha
     * @param lineName nome da linha
     */
    suspend fun checkArrival(
        departureStop: String,
        destination: String,
        lineCode: String,
        lineName: String
    ): BusArrivalResult? {
        if (departureStop.isBlank() || destination.isBlank()) {
            Log.w(TAG, "departureStop ou destination vazio para $lineCode")
            return null
        }

        val request = RouteRequest(
            origin = AdressRequest(departureStop),
            destination = AdressRequest("Terminal$destination"),
            travelMode = "TRANSIT",
            computeAlternativeRoutes = true,
            transitPreferences = TransitPreferences(
                routingPreference = "TRANSIT_ROUTING_PREFERENCE_UNSPECIFIED",
                allowedTravelModes = listOf("BUS")
            )
        )

        Log.d(TAG, "=== ENVIANDO REQUISIÇÃO PARA ROUTES API ===")
        Log.d(TAG, "  Linha     : $lineCode ($lineName)")
        Log.d(TAG, "  Origem    : $departureStop")
        Log.d(TAG, "  Destino   : Terminal$destination")
        Log.d(TAG, "  Corpo     : $request")

        return try {
            val response = apiService.searchPlaces(
                apiKey = BuildConfig.GOOGLE_API_KEY,
                request = request
            )

            Log.d(TAG, "  ✓ Resposta recebida. Rotas: ${response.routes.size}")

            val transitDetail = response.routes
                .firstOrNull()
                ?.legs?.firstOrNull()
                ?.steps?.firstOrNull { it.transitDetails != null }
                ?.transitDetails

            if (transitDetail == null) {
                Log.w(TAG, "Nenhum trânsito encontrado para $lineCode")
                return BusArrivalResult(
                    lineCode = lineCode,
                    lineName = lineName,
                    departureStop = departureStop,
                    arrivalStop = "",
                    minutesUntilArrival = -1,
                    hasPassedStop = true
                )
            }

            val arrivalTimeStr = transitDetail.stopDetails.departureTime
            val minutesUntil = parseMinutesUntil(arrivalTimeStr)

            Log.d(TAG, "Linha $lineCode: chega em $minutesUntil min (raw: $arrivalTimeStr)")

            BusArrivalResult(
                lineCode = lineCode,
                lineName = lineName,
                departureStop = transitDetail.stopDetails.departureStop.name,
                arrivalStop = transitDetail.stopDetails.arrivalStop.name,
                minutesUntilArrival = minutesUntil,
                hasPassedStop = minutesUntil < -2 // margem de 2min
            )
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao checar linha $lineCode", e)
            null
        }
    }

    /**
     * Parseia o tempo da API e calcula minutos até a chegada.
     */
    private fun parseMinutesUntil(timeStr: String): Long {
        val trimmed = timeStr.trim()
        if (trimmed.isEmpty()) return -1

        // Tenta ISO ZonedDateTime / Instant
        try {
            val instant = Instant.parse(trimmed)
            val now = Instant.now()
            return Duration.between(now, instant).toMinutes()
        } catch (_: Exception) {}

        try {
            val zdt = ZonedDateTime.parse(trimmed)
            val now = ZonedDateTime.now(ZoneId.systemDefault())
            return Duration.between(now, zdt.withZoneSameInstant(ZoneId.systemDefault())).toMinutes()
        } catch (_: Exception) {}

        // Tenta parsear como LocalTime
        val formatters = listOf(
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("HH:mm:ss")
        )

        val nowLocal = LocalDateTime.now()

        for (fmt in formatters) {
            try {
                val parsedTime = LocalTime.parse(trimmed, fmt)
                var arrivalDateTime = LocalDateTime.of(LocalDate.now(), parsedTime)
                if (arrivalDateTime.isBefore(nowLocal.minusMinutes(1))) {
                    arrivalDateTime = arrivalDateTime.plusDays(1)
                }
                return Duration.between(nowLocal, arrivalDateTime).toMinutes()
            } catch (_: DateTimeParseException) {}
        }

        // Último recurso: extrair dígitos
        val digits = Regex("(\\d+)").find(trimmed)?.value?.toLongOrNull()
        return digits ?: -1
    }
}
