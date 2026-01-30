package com.will.busnotification.data.dto

// Request shape matching Google Routes API (computeRoutes) — use location.latLng
data class RouteRequest(
    val origin: RouteLocation,
    val destination: RouteLocation,
    val travelMode: String = "TRANSIT",
    val computeAlternativeRoutes: Boolean = true,
    val transitPreferences: TransitPreferences? = null
)

// Wrapper for location with latLng
data class RouteLocation(
    val location: LocationLatLng
)

data class LocationLatLng(
    val latLng: LatLng
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)

// Transit preferences
data class TransitPreferences(
    val routingPreference: String,
    val allowedTravelModes: List<String>
)