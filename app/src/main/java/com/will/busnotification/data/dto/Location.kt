package com.will.busnotification.data.dto

/**
 * Representação simples de coordenadas geográficas
 */
data class LatLng(
    val latitude: Double,
    val longitude: Double
)

/**
 * Input para requisições de rota - suporta endereço ou coordenadas
 */
data class LocationInput(
    val address: String? = null,
    val latLng: LatLng? = null
)

