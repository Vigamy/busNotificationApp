package com.will.busnotification.data.model

data class Bus(
    val lineName: String,
    val lineShortName: String,
    val destination: String,
    val departureStop: String,
    val departureTime: String,
    val arrivalStop: String,
    val arrivalTime: String,
    val color: Int,
    val textColor: Int
)
