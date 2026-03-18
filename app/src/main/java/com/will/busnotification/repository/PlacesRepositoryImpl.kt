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

    companion object {
        private const val TAG = "PlacesRepository"
    }

    override suspend fun searchPlaces(query: String): List<TransitSegment> {
        val apiKey = BuildConfig.GOOGLE_API_KEY

        val origin = resolveOrigin()
            ?: throw IllegalStateException(
                "Não foi possível obter a localização atual. Verifique se a permissão de localização foi concedida."
            )

        // 1st attempt: BUS only
        val busResults = fetchRoutes(
            apiKey = apiKey,
            origin = origin,
            destination = AdressRequest.fromAddress(query),
            allowedModes = listOf("BUS")
        )

        if (busResults.isNotEmpty()) {
            Log.d(TAG, "Found ${busResults.size} BUS-only results for '$query'")
            return busResults
        }

        // 2nd attempt: any TRANSIT mode (bus + metro + train + tram)
        Log.d(TAG, "No BUS-only results for '$query' — retrying with all transit modes")
        val allTransitResults = fetchRoutes(
            apiKey = apiKey,
            origin = origin,
            destination = AdressRequest.fromAddress(query),
            allowedModes = null // no filter → accepts bus, metro, train, tram
        )

        if (allTransitResults.isNotEmpty()) {
            Log.d(TAG, "Found ${allTransitResults.size} results with all transit modes for '$query'")
        } else {
            Log.w(TAG, "No transit results at all for '$query'")
        }

        return allTransitResults
    }

    /**
     * Calls the Google Routes API with the given parameters.
     * @param allowedModes if null, no filter is applied (all transit modes)
     */
    private suspend fun fetchRoutes(
        apiKey: String,
        origin: AdressRequest,
        destination: AdressRequest,
        allowedModes: List<String>?
    ): List<TransitSegment> {
        val transitPrefs = if (allowedModes != null) {
            TransitPreferences(
                routingPreference = "TRANSIT_ROUTING_PREFERENCE_UNSPECIFIED",
                allowedTravelModes = allowedModes
            )
        } else {
            null
        }

        val request = RouteRequest(
            origin = origin,
            destination = destination,
            travelMode = "TRANSIT",
            computeAlternativeRoutes = true,
            transitPreferences = transitPrefs
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
            Log.e(TAG, "HttpException (${e.code()}): $errBody", e)
            throw e
        } catch (e: IOException) {
            Log.e(TAG, "Network I/O error", e)
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
                Log.d(TAG, "Using device location: ${loc.latitude}, ${loc.longitude}")
                AdressRequest.fromLatLng(loc.latitude, loc.longitude)
            } else {
                Log.w(TAG, "Location unavailable — getLastKnownLocation returned null")
                null
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to get location", e)
            null
        }
    }
}
