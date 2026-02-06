package com.will.busnotification.viewmodel

import androidx.lifecycle.ViewModel
import com.will.busnotification.BuildConfig
import com.will.busnotification.data.api.GoogleApiInstance
import com.will.busnotification.data.dto.LocationInput
import com.will.busnotification.data.dto.RouteRequest
import com.will.busnotification.data.dto.RouteResponse
import com.will.busnotification.data.dto.TransitPreferences
import com.will.busnotification.data.model.Bus
import com.will.busnotification.repository.toBusList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.DocumentSnapshot
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusViewModel : ViewModel() {
    private val _busList = MutableStateFlow<List<Bus>>(emptyList())
    private val _notifiedBuses = MutableStateFlow<List<Bus>>(emptyList())
    val busList: StateFlow<List<Bus>> = _busList
    val notifiedBusList: StateFlow<List<Bus>> = _notifiedBuses

    private val firestore = Firebase.firestore

    fun loadBusFromFirebase() {
        val locationsRef = firestore.collection("locations")
        val originDoc = locationsRef.document("origin")
        val destDoc = locationsRef.document("destination")

        originDoc.get().addOnSuccessListener { originSnap: DocumentSnapshot ->
            destDoc.get().addOnSuccessListener { destSnap: DocumentSnapshot ->
                val originAddress = originSnap.getString("address") ?: ""
                val destAddress = destSnap.getString("address") ?: ""

                // Ajuste aqui os parâmetros de LocationInput conforme sua implementação real.
                val originInput = LocationInput(address = originAddress)
                val destinationInput = LocationInput(address = destAddress)

                val requestBody = RouteRequest(
                    origin = originInput,
                    destination = destinationInput,
                    travelMode = "TRANSIT",
                    computeAlternativeRoutes = true,
                    transitPreferences = TransitPreferences(
                        routingPreference = "TRANSIT_ROUTING_PREFERENCE_UNSPECIFIED",
                        allowedTravelModes = listOf("BUS")
                    )
                )

                GoogleApiInstance.retrofit.getBus(requestBody, apiKey = BuildConfig.GOOGLE_API_KEY)
                    .enqueue(object : Callback<RouteResponse> {
                        override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    _busList.value = it.toBusList()
                                }
                            }
                        }

                        override fun onFailure(p0: Call<RouteResponse>, p1: Throwable) {
                            p1.message?.let { println(it) }
                        }
                    })
            }.addOnFailureListener { e: Exception ->
                println("Erro ao ler destination: ${e.message}")
            }
        }.addOnFailureListener { e: Exception ->
            println("Erro ao ler origin: ${e.message}")
        }
    }

    fun loadBus() {
        val requestBody = RouteRequest(
            origin = LocationInput("Rua Gen. Mello Rezende, 11"),
            destination = LocationInput("R. John Harrison"),
            travelMode = "TRANSIT",
            computeAlternativeRoutes = true,
            transitPreferences = TransitPreferences(
                routingPreference = "TRANSIT_ROUTING_PREFERENCE_UNSPECIFIED",
                allowedTravelModes = listOf("BUS")
            )
        )
        GoogleApiInstance.retrofit.getBus(requestBody, apiKey = BuildConfig.GOOGLE_API_KEY)
            .enqueue(object : Callback<RouteResponse> {
                override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _busList.value = it.toBusList()
                        }
                    }
                }

                override fun onFailure(p0: Call<RouteResponse>, p1: Throwable) {
                    if (p1.message != null) {
                        println(p1.message)
                    }
                }
            })
    }

    fun loadNotifiedBuses() {
        // Implement loading of notified buses from storage or database
        _notifiedBuses.value = listOf(
            // Example data
            Bus(
                "Bus 1", "Route A", "10:00 AM",
                departureStop = "Main Street",
                departureTime = "09:50 AM",
                arrivalStop = "Central Station",
                arrivalTime = "10:30 AM",
                color = 0xFF2196F3.toInt(),
                textColor = 0xFFFFFFFF.toInt()
            ),
            Bus(
                "Bus 2", "Route B", "11:00 AM",
                departureStop = "2nd Ave",
                departureTime = "10:50 AM",
                arrivalStop = "Downtown",
                arrivalTime = "11:20 AM",
                color = 0xFF4CAF50.toInt(),
                textColor = 0xFF000000.toInt()
            )
        )
    }
}