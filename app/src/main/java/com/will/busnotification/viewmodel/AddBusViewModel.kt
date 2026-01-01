package com.will.busnotification.viewmodel

import androidx.lifecycle.ViewModel
import com.will.busnotification.data.model.Bus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddBusViewModel : ViewModel() {

    private val _busList = MutableStateFlow<List<Bus>>(emptyList())
    val busList: StateFlow<List<Bus>> = _busList

    

}