package com.will.busnotification.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.will.busnotification.data.model.Bus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BusViewModel : ViewModel() {
    private val _busList = MutableStateFlow<List<Bus>>(emptyList())
    val busList: StateFlow<List<Bus>> = _busList

    fun loadBus() {
        // Aqui você teria que instanciar o repository manualmente ou simular os dados
        val fakeList = listOf(
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            ),
            Bus(
                "118Y-10",
                "Lauzane Paulista",
                "Lapa",
                "14:30",
                "Rua George Smith, 80",
                "14:51",
                "#005e98",
                "#FFF",
                "#FFF"
            )
        )
        _busList.value = fakeList
    }
}