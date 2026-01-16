package com.will.busnotification.repository

import android.graphics.Color
import com.will.busnotification.data.dto.RouteResponse
import com.will.busnotification.data.model.Bus


fun RouteResponse.toBusList(): List<Bus> {
    return routes.mapNotNull { route ->
        val step = route.legs.firstOrNull()?.steps?.firstOrNull { it.transitDetails != null }
        val details = step?.transitDetails ?: return@mapNotNull null

        val colorInt = runCatching { Color.parseColor(details.transitLine.color) }.getOrDefault(Color.BLACK)
        val textColorInt = runCatching { Color.parseColor(details.transitLine.textColor) }.getOrDefault(Color.WHITE)

        Bus(
            lineName = details.transitLine.name,
            lineShortName = details.transitLine.nameShort,
            destination = details.headsign,
            departureStop = details.stopDetails.departureStop.name,
            departureTime = details.localizedValues.departureTime.time.text,
            arrivalStop = details.stopDetails.arrivalStop.name,
            arrivalTime = details.localizedValues.arrivalTime.time.text,
            color = colorInt,
            textColor = textColorInt
        )
    }
}
