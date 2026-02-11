package com.will.busnotification.data.dto

/**
 * Requisição para a Google Routes API
 * @param origin Localização de origem (endereço ou coordenadas)
 * @param destination Localização de destino (endereço ou coordenadas)
 * @param travelMode Modo de viagem (ex: TRANSIT, DRIVE)
 * @param computeAlternativeRoutes Se deve calcular rotas alternativas
 * @param transitPreferences Preferências para transporte público
 */
data class RouteRequest(
    val origin: AdressRequest,
    val destination: AdressRequest,
    val travelMode: String = "TRANSIT",
    val computeAlternativeRoutes: Boolean = true,
    val transitPreferences: TransitPreferences? = null
)

/**
 * Preferências para cálculo de rotas de transporte público
 */
data class TransitPreferences(
    val routingPreference: String,
    val allowedTravelModes: List<String>
)