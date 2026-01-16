package com.will.busnotification.data.dto

data class RouteResponse(
    val routes: List<Route>
)

data class Route(
    val legs: List<Leg>
)

data class Leg(
    val steps: List<Step>
)

data class Step(
    val transitDetails: TransitDetails?
)

data class TransitDetails(
    val stopDetails: StopDetails,
    val localizedValues: LocalizedValues,
    val headsign: String,
    val transitLine: TransitLine,
    val stopCount: Int
)

data class StopDetails(
    val arrivalStop: StopInfo,
    val departureStop: StopInfo,
    val arrivalTime: String,
    val departureTime: String
)

data class StopInfo(
    val name: String,
    val location: RouteLocation
)

// Renamed from Location to RouteLocation to avoid redeclaration with Location.kt
data class RouteLocation(
    val latLng: LatLng
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)

data class LocalizedValues(
    val arrivalTime: LocalizedTime,
    val departureTime: LocalizedTime
)

data class LocalizedTime(
    val time: TimeText,
    val timeZone: String
)

data class TimeText(
    val text: String
)

data class TransitLine(
    val name: String,
    val nameShort: String,
    val headsign: String?,
    val color: String,
    val textColor: String,
    val agencies: List<Agency>,
    val vehicle: Vehicle
)

data class Agency(
    val name: String,
    val uri: String
)

data class Vehicle(
    val name: VehicleName,
    val type: String,
    val iconUri: String
)

data class VehicleName(
    val text: String
)
