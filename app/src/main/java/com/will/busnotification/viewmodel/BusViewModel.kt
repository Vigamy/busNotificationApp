package com.will.busnotification.viewmodel

import androidx.lifecycle.ViewModel
import com.will.busnotification.data.api.GoogleApiInstance
import com.will.busnotification.data.dto.LocationInput
import com.will.busnotification.data.dto.RouteRequest
import com.will.busnotification.data.dto.RouteResponse
import com.will.busnotification.data.dto.TransitPreferences
import com.will.busnotification.data.model.Bus
import com.will.busnotification.repository.toBusList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusViewModel : ViewModel() {
    private val _busList = MutableStateFlow<List<Bus>>(emptyList())
    val busList: StateFlow<List<Bus>> = _busList

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
        GoogleApiInstance.retrofit.getBus(requestBody, apiKey = "AIzaSyB1bNCyVsnT0fFQsXH2RsmnlXiQzqGKHU8")
            .enqueue(object : Callback<RouteResponse> {
                override fun onResponse(call: Call<RouteResponse?>, response: Response<RouteResponse?>) {
                    if (response.isSuccessful) {
                        val buses = response.body()?.toBusList() ?: emptyList()
                        _busList.value = buses
                    }
                }

                override fun onFailure(p0: Call<RouteResponse?>, p1: Throwable) {
                    if (p1.message != null) {
                        println(p1.message)
                    }
                }
            })
    }
}