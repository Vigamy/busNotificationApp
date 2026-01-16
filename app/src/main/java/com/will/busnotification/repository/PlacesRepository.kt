package com.will.busnotification.repository

import com.will.busnotification.data.dto.PlaceResult

interface PlacesRepository {
    suspend fun searchPlaces(query: String): List<PlaceResult>
}
