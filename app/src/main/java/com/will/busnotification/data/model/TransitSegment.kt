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
