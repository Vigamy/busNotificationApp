package com.will.busnotification.data.network

import com.will.busnotification.data.dto.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApiService {

    @GET("maps/api/place/textsearch/json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("key") apiKey: String // Lembre-se de substituir pela sua chave de API
    ): PlacesResponse
}
