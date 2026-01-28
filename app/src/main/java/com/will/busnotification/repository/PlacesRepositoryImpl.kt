package com.will.busnotification.repository

import com.will.busnotification.BuildConfig
import com.will.busnotification.data.dto.PlacesResponse
import com.will.busnotification.data.dto.PlaceResult
import com.will.busnotification.data.network.GooglePlacesApiService
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val apiService: GooglePlacesApiService
) : PlacesRepository {

    override suspend fun searchPlaces(query: String): List<PlaceResult> {
        val apiKey = BuildConfig.GOOGLE_API_KEY
        val response: PlacesResponse = apiService.searchPlaces(query = query, apiKey = apiKey)
        return response.results
    }
}
