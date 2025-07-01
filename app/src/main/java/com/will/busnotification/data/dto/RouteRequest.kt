package com.will.busnotification.data.dto


data class RouteRequest(
    val origin: LocationInput,
    val destination: LocationInput,
    val travelMode: String = "TRANSIT",
    val computeAlternativeRoutes: Boolean = true,
    val transitPreferences: TransitPreferences
)

data class LocationInput(val address: String)

data class TransitPreferences(
    val routingPreference: String,
    val allowedTravelModes: List<String>
)