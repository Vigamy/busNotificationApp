package com.will.busnotification.repository

import com.will.busnotification.data.model.TransitSegment

interface PlacesRepository {
    suspend fun searchPlaces(query: String): List<TransitSegment>
}
