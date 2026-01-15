package com.will.busnotification.repository

import com.will.busnotification.data.dto.PlacesResponse
import com.will.busnotification.data.network.GooglePlacesApiService
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val apiService: GooglePlacesApiService
) : PlacesRepository {

    override suspend fun searchPlaces(query: String): PlacesResponse {
        // TODO: Substitua "SUA_CHAVE_DE_API_AQUI" pela sua chave de API do Google Places.
        // Lembre-se de que esta não é uma prática segura para produção.
        // A chave deve ser armazenada de forma segura (ex: build.gradle ou local.properties).
        val apiKey = "SUA_CHAVE_DE_API_AQUI"
        return apiService.searchPlaces(query = query, apiKey = apiKey)
    }
}
