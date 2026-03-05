package com.will.busnotification.data.dto

import com.will.busnotification.data.model.NotificationWindow

data class NotificationSetupPayload(
    val lineCode: String,
    val lineName: String,
    val departureStop: String,
    val arrivalStop: String,
    val destination: String,
    val notificationWindow: NotificationWindow
)
