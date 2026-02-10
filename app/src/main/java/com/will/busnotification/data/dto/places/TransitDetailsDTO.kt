package com.will.busnotification.data.dto.places

data class TransitDetailsDTO {
    val headsign: String? = ""
    val stopCount: Int? = 1
    val stopDetails: StopDetailsDto? = null
    val transitLine: TransitLineDto? = null
}
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
