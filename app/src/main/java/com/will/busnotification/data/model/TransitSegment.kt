package com.will.busnotification.data.model

data class TransitSegment(
    val lineCode: String,
    val lineName: String,
    val headsign: String,
    val departureStop: String,
    val arrivalStop: String,
    val departureTime: String,
    val arrivalTime: String,
    val stopCount: Int
)
fun DirectionsResponseDto.toTransitSegments(): List<TransitSegment> {
    return routes.flatMap { route ->
        route.legs.flatMap { leg ->
            leg.steps.mapNotNull { step ->
                val td = step.transitDetails ?: return@mapNotNull null

                TransitSegment(
                    lineCode = td.transitLine.nameShort,
                    lineName = td.transitLine.name,
                    headsign = td.headsign.orEmpty(),
                    departureStop = td.stopDetails.departureStop.name,
                    arrivalStop = td.stopDetails.arrivalStop.name,
                    departureTime = td.stopDetails.departureTime,
                    arrivalTime = td.stopDetails.arrivalTime,
                    stopCount = td.stopCount ?: 0
                )
            }
        }
    }
}
