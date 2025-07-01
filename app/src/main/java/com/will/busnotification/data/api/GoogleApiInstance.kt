package com.will.busnotification.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleApiInstance {

    private const val BASE_URL = "https://routes.googleapis.com/directions/v2:computeRoutes/"

//    fun getInstance(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    val retrofit: GoogleApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleApiInterface::class.java)
    }


}