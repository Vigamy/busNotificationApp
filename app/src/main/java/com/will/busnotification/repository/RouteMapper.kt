package com.will.busnotification.repository

import android.graphics.Color
import com.will.busnotification.data.dto.RouteResponse
import com.will.busnotification.data.model.Bus
import androidx.core.graphics.toColorInt


fun RouteResponse.toBusList(): List<Bus> {
    return routes?.mapNotNull { route ->
        val step = route.legs?.firstOrNull()?.steps?.firstOrNull { it.transitDetails != null }
        val details = step?.transitDetails ?: return@mapNotNull null

        val colorInt = runCatching { details.transitLine.color.toColorInt() }.getOrDefault(Color.BLACK)
        val textColorInt = runCatching { details.transitLine.textColor.toColorInt() }.getOrDefault(Color.WHITE)

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
    } ?: emptyList()
}
