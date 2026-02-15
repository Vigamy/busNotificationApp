package com.will.busnotification.data.mapper

import com.will.busnotification.data.dto.DirectionsResponseDto
import com.will.busnotification.data.model.TransitSegment

fun DirectionsResponseDto.toTransitSegments(): List<TransitSegment> {
    return routes?.flatMap { route ->
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
    } ?: emptyList()
}