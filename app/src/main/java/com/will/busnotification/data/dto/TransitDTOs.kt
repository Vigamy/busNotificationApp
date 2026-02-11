package com.will.busnotification.data.dto

data class DirectionsResponseDto(
    val routes: List<RouteDto>
)

data class RouteDto(
    val legs: List<LegDto>
)

data class LegDto(
    val steps: List<StepDto>
)

data class StepDto(
    val transitDetails: TransitDetailsDto?
)

data class TransitDetailsDto(
    val headsign: String?,
    val stopCount: Int?,
    val stopDetails: StopDetailsDto,
    val transitLine: TransitLineDto
)

data class StopDetailsDto(
    val departureStop: StopDto,
    val arrivalStop: StopDto,
    val departureTime: String,
    val arrivalTime: String
)

data class StopDto(
    val name: String
)

data class TransitLineDto(
    val nameShort: String,
    val name: String,
    val color: String
)
