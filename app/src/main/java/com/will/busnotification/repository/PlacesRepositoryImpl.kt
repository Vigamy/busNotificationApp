package com.will.busnotification.repository

import android.util.Log
import com.will.busnotification.BuildConfig
import com.will.busnotification.data.dto.AdressRequest
import com.will.busnotification.data.dto.PlaceResult
import com.will.busnotification.data.dto.PlacesResponse
import com.will.busnotification.data.dto.RouteRequest
import com.will.busnotification.data.dto.TransitPreferences
import com.will.busnotification.data.network.GooglePlacesApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val apiService: GooglePlacesApiService,
    private val locationProvider: LocationProvider
) : PlacesRepository {

    override suspend fun searchPlaces(query: String): List<PlaceResult> {
        val apiKey = BuildConfig.GOOGLE_API_KEY

        // Get current location (lat/lng) — caller must ensure permissions
        val loc = try {
            locationProvider.getLastKnownLocation()
        } catch (_: Throwable) {
            null
        }

        val request = RouteRequest(
            origin = (
                    AdressRequest("Rua são ladislau, 141")
            ),
            destination = (
                    AdressRequest(query)
            ),
            travelMode = "TRANSIT",
            computeAlternativeRoutes = true,
            transitPreferences = TransitPreferences(
                routingPreference = "TRANSIT_ROUTING_PREFERENCE_UNSPECIFIED",
                allowedTravelModes = listOf("BUS")
            )
        )

        try {
            val response: PlacesResponse = apiService.searchPlaces(apiKey = apiKey, request = request)
            return response.results
        } catch (e: HttpException) {
            // Try to log the server error body for 4xx/5xx
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
}
