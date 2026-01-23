package com.will.busnotification.data.api

import com.will.busnotification.data.dto.RouteRequest
import com.will.busnotification.data.dto.RouteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GoogleApiInterface {

    @POST("https://routes.googleapis.com/directions/v2:computeRoutes")
    fun getBus(
        @Body body: RouteRequest,
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String = "routes.legs.steps.transitDetails",
    ): Call<RouteResponse>


}