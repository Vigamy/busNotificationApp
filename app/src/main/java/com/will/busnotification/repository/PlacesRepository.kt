package com.will.busnotification.repository

import com.will.busnotification.data.dto.PlacesResponse

interface PlacesRepository {
    suspend fun searchPlaces(query: String): PlacesResponse
}
