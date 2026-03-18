package com.will.busnotification.data.dto

/**
 * Waypoint for the Google Routes API.
 * Accepts either a textual [address] OR a [location] with lat/lng coordinates.
 * When both are provided the API prefers [location].
 */
data class AdressRequest(
    val address: String? = null,
    val location: LatLngWrapper? = null,
) {
    companion object {
        /** Create a waypoint from a textual address. */
        fun fromAddress(address: String) = AdressRequest(address = address)

        /** Create a waypoint from latitude/longitude coordinates. */
        fun fromLatLng(lat: Double, lng: Double) = AdressRequest(
            location = LatLngWrapper(latLng = LatLngCoord(latitude = lat, longitude = lng))
        )
    }
}

data class LatLngWrapper(
    val latLng: LatLngCoord
)

data class LatLngCoord(
    val latitude: Double,
    val longitude: Double
)
