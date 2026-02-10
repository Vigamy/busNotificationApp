package com.will.busnotification.data.network

import com.will.busnotification.data.dto.places.PlacesResponse
import com.will.busnotification.data.dto.RouteRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GooglePlacesApiService {

    // Use the Routes API host (routes.googleapis.com) and the X-Goog-Api-Key header
    @POST("directions/v2:computeRoutes")
    suspend fun searchPlaces(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String = "routes.duration,routes.distanceMeters",
        @Body request: RouteRequest
    ): PlacesResponse
}
