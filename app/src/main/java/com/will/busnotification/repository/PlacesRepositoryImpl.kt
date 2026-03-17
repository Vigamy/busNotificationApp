package com.will.busnotification.repository

import android.util.Log
import com.will.busnotification.BuildConfig
import com.will.busnotification.data.dto.AdressRequest
import com.will.busnotification.data.dto.DirectionsResponseDto
import com.will.busnotification.data.dto.RouteRequest
import com.will.busnotification.data.dto.TransitPreferences
import com.will.busnotification.data.mapper.toTransitSegments
import com.will.busnotification.data.model.TransitSegment
import com.will.busnotification.data.network.GooglePlacesApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val apiService: GooglePlacesApiService,
    private val locationProvider: LocationProvider
) : PlacesRepository {

    override suspend fun searchPlaces(query: String): List<TransitSegment> {
        val apiKey = BuildConfig.GOOGLE_API_KEY

        val origin = resolveOrigin()
            ?: throw IllegalStateException(
                "Não foi possível obter a localização atual. Verifique se a permissão de localização foi concedida."
            )

        val request = RouteRequest(
            origin = origin,
            destination = AdressRequest.fromAddress(query),
            travelMode = "TRANSIT",
            computeAlternativeRoutes = true,
            transitPreferences = TransitPreferences(
                routingPreference = "TRANSIT_ROUTING_PREFERENCE_UNSPECIFIED",
                allowedTravelModes = listOf("BUS")
            )
        )

        try {
            val response: DirectionsResponseDto =
                apiService.searchPlaces(apiKey = apiKey, request = request)
            return response.toTransitSegments()
        } catch (e: HttpException) {
            val errBody = try {
                e.response()?.errorBody()?.string()
            } catch (_: Throwable) {
                "<failed to read error body>"
            }
            Log.e("PlacesRepository", "HttpException (${e.code()}): $errBody", e)
            throw e
        } catch (e: IOException) {
            Log.e("PlacesRepository", "Network I/O error", e)
            throw e
        }
    }

    /**
     * Tries to resolve the user's current location as a Routes API origin.
     * Prefers GPS coordinates; falls back to null if unavailable.
     */
    private fun resolveOrigin(): AdressRequest? {
        return try {
            val loc = locationProvider.getLastKnownLocation()
            if (loc != null) {
                Log.d("PlacesRepository", "Using device location: ${loc.latitude}, ${loc.longitude}")
                AdressRequest.fromLatLng(loc.latitude, loc.longitude)
            } else {
                Log.w("PlacesRepository", "Location unavailable — getLastKnownLocation returned null")
                null
            }
        } catch (e: Throwable) {
            Log.e("PlacesRepository", "Failed to get location", e)
            null
        }
    }
}
