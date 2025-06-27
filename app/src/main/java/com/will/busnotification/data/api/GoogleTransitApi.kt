package com.will.busnotification.data.api

import com.will.busnotification.data.model.Bus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GoogleTransitApi {
    @POST("https://routes.googleapis.com/directions/v2:computeRoutes")
    suspend fun getBus(
        @Header("X-Goog-Api-Key") apiKey: String = "AIzaSyB1bNCyVsnT0fFQsXH2RsmnlXiQzqGKHU8",
        @Header("X-Goog-FieldMask") fieldMask: String = "routes.legs.steps.transitDetails",
        @Body requestBody: Bus
    ): Response<List<Bus>>
}