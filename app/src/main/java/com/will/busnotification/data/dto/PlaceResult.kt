package com.will.busnotification.data.dto

import com.google.gson.annotations.SerializedName

data class PlaceResult(
    val name: String,
    val geometry: Geometry,
    @SerializedName("place_id") val placeId: String
)
