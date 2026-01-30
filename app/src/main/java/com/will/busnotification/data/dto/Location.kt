package com.will.busnotification.data.dto

data class LatLng(
    val latitude: Double,
    val longitude: Double
)

data class LocationDto(
    val latLng: LatLng
)

data class Waypoint(
    val location: LocationDto
)
