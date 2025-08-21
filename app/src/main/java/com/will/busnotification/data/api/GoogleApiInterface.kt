package com.will.busnotification.data.api

import com.will.busnotification.data.dto.RouteRequest
import com.will.busnotification.data.dto.RouteResponse
import com.will.busnotification.data.model.Bus
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GoogleApiInterface {

    @POST("https://routes.googleapis.com/directions/v2:computeRoutes")
    fun getBus(
        @Body body: RouteRequest,
        @Header("X-Goog-Api-Key") apiKey: String = "AIzaSyB1bNCyVsnT0fFQsXH2RsmnlXiQzqGKHU8",
        @Header("X-Goog-FieldMask") fieldMask: String = "routes.legs.steps.transitDetails",
    ): Call<RouteResponse>

//    @POST("https://routes.googleapis.com/directions/v2:computeRoutes")
//    fun getBus(
//        @Header("X-Goog-Api-Key") apiKey: String = "AIzaSyB1bNCyVsnT0fFQsXH2RsmnlXiQzqGKHU8",
//        @Header("X-Goog-FieldMask") fieldMask: String = "routes.legs.steps.transitDetails",
//        @Body busRequestDTO: RouteRequest
//    ): Call<List<Bus>>

}