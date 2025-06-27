package com.will.busnotification.data.api

import com.will.busnotification.data.model.Bus
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

object GoogleApiClient {

    private const val BASE_URL = "https://routes.googleapis.com/directions/v2"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GoogleTransitApi by lazy {
        retrofit.create(GoogleTransitApi::class.java)
    }

}