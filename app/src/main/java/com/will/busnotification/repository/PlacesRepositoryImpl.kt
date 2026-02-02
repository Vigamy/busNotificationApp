package com.will.busnotification.repository

import android.util.Log
import com.will.busnotification.BuildConfig
import com.will.busnotification.data.dto.LatLng
import com.will.busnotification.data.dto.LocationInput
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

        val originLat = loc?.latitude ?: 0.0
        val originLng = loc?.longitude ?: 0.0

        // Try to geocode destination query text to lat/lng
        val destCoords = locationProvider.geocodeAddress(query)
        val destLat = destCoords?.first ?: originLat
        val destLng = destCoords?.second ?: originLng

        // If both origin and destination are 0.0 (no location and no geocode), don't call API — avoid HTTP 400
        if (originLat == 0.0 && originLng == 0.0 && destLat == 0.0 && destLng == 0.0) {
            Log.w("PlacesRepository", "No valid coordinates for origin or destination — skipping API call")
            return emptyList()
        }

        val request = RouteRequest(
            origin = LocationInput(
                latLng = LatLng(
                    latitude = originLat,
                    longitude = originLng
                )
            ),
            destination = LocationInput(
                latLng = LatLng(
                    latitude = destLat,
                    longitude = destLng
                )
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
