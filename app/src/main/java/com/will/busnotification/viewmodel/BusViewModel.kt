package com.will.busnotification.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.will.busnotification.BuildConfig
import com.will.busnotification.data.dto.AdressRequest
import com.will.busnotification.data.dto.RouteRequest
import com.will.busnotification.data.dto.TransitPreferences
import com.will.busnotification.data.model.TransitSegment
import com.will.busnotification.data.network.GooglePlacesApiService
import com.will.busnotification.repository.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val apiService: GooglePlacesApiService,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _busList = MutableStateFlow<List<TransitSegment>>(emptyList())
    val busList: StateFlow<List<TransitSegment>> = _busList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val firestore = Firebase.firestore

    companion object {
        private const val TAG = "BusViewModel"
    }

    fun loadBusFromFirebase() {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "=== INICIANDO CARREGAMENTO DOS ÔNIBUS SALVOS ===")

            try {
                // Resolve the user's current location to use as origin
                val origin = resolveCurrentOrigin()
                if (origin == null) {
                    Log.w(TAG, "⚠ Localização atual indisponível — não é possível calcular rotas.")
                    _busList.value = emptyList()
                    return@launch
                }

                val snapshot = firestore.collection("locations").get().await()
                val documents = snapshot.documents
                Log.d(TAG, "Total de ônibus salvos no Firestore: ${documents.size}")

                if (documents.isEmpty()) {
                    Log.d(TAG, "Nenhum ônibus salvo encontrado.")
                    _busList.value = emptyList()
                    return@launch
                }

                val results = mutableListOf<TransitSegment>()

                for (doc in documents) {
                    val lineCode = doc.getString("lineCode") ?: doc.id
                    val destination = doc.getString("destination") ?: ""

                    Log.d(TAG, "--- Processando linha: $lineCode ---")
                    Log.d(TAG, "  Destino (destination): '$destination'")

                    if (destination.isBlank()) {
                        Log.w(TAG, "  ⚠ Destino ausente para linha '$lineCode' — pulando.")
                        continue
                    }

                    val request = RouteRequest(
                        origin = origin,
                        destination = AdressRequest.fromAddress(destination),
                        travelMode = "TRANSIT",
                        computeAlternativeRoutes = true,
                        transitPreferences = TransitPreferences(
                            routingPreference = "TRANSIT_ROUTING_PREFERENCE_UNSPECIFIED",
                            allowedTravelModes = listOf("BUS")
                        )
                    )

                    Log.d(TAG, "  Enviando requisição para Google Routes API...")
                    Log.d(TAG, "  Request body: $request")

                    try {
                        val response = apiService.searchPlaces(
                            apiKey = BuildConfig.GOOGLE_API_KEY,
                            request = request
                        )

                        Log.d(TAG, "  Resposta recebida. Total de rotas: ${response.routes.size}")

                        val firstSegment = response.routes
                            .firstOrNull()
                            ?.legs?.firstOrNull()
                            ?.steps?.firstOrNull { it.transitDetails != null }
                            ?.transitDetails
                            ?.let { td ->
                                TransitSegment(
                                    lineCode = td.transitLine.nameShort,
                                    lineName = td.transitLine.name,
                                    headsign = td.headsign.orEmpty(),
                                    departureStop = td.stopDetails.departureStop.name,
                                    arrivalStop = td.stopDetails.arrivalStop.name,
                                    departureTime = td.stopDetails.departureTime,
                                    arrivalTime = td.stopDetails.arrivalTime,
                                    stopCount = td.stopCount ?: 0
                                )
                            }

                        if (firstSegment != null) {
                            Log.d(TAG, "  ✓ Próximo ônibus encontrado: ${firstSegment.lineCode} — ${firstSegment.lineName}")
                            Log.d(TAG, "    Partida: ${firstSegment.departureStop} às ${firstSegment.departureTime}")
                            Log.d(TAG, "    Chegada: ${firstSegment.arrivalStop} às ${firstSegment.arrivalTime}")
                            results.add(firstSegment)
                        } else {
                            Log.w(TAG, "  ⚠ Nenhum segmento de ônibus encontrado na resposta para linha '$lineCode'.")
                        }

                    } catch (e: HttpException) {
                        val errBody = try { e.response()?.errorBody()?.string() } catch (_: Throwable) { "<erro ao ler body>" }
                        Log.e(TAG, "  ✗ HttpException (${e.code()}) para linha '$lineCode': $errBody", e)
                    } catch (e: Exception) {
                        Log.e(TAG, "  ✗ Erro inesperado ao buscar linha '$lineCode'", e)
                    }
                }

                Log.d(TAG, "=== CARREGAMENTO CONCLUÍDO: ${results.size} ônibus carregados ===")
                _busList.value = results

            } catch (e: Exception) {
                Log.e(TAG, "✗ Erro ao buscar documentos do Firestore", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Resolves the user's current location into a Routes API origin waypoint.
     * Returns null if location is unavailable.
     */
    private fun resolveCurrentOrigin(): AdressRequest? {
        return try {
            val loc = locationProvider.getLastKnownLocation()
            if (loc != null) {
                Log.d(TAG, "Using device location: ${loc.latitude}, ${loc.longitude}")
                AdressRequest.fromLatLng(loc.latitude, loc.longitude)
            } else {
                null
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to get location", e)
            null
        }
    }
}
