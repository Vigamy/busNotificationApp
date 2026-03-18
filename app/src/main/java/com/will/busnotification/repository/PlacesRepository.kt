package com.will.busnotification.repository

import com.will.busnotification.data.model.TransitSegment

interface PlacesRepository {
    /**
     * Search for transit routes to [query] destination.
     * Returns a list of transit segments found, or empty if no routes available.
     * May throw IOException on network errors or IllegalStateException if location is unavailable.
     */
    suspend fun searchPlaces(query: String): List<TransitSegment>
}
