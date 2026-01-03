package com.will.busnotification.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.will.busnotification.data.model.Bus
import com.will.busnotification.ui.components.BusItemComponent
import com.will.busnotification.ui.components.BusItemLineComponent
import com.will.busnotification.ui.components.HeaderComponent
import com.will.busnotification.viewmodel.AddBusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun AddBusScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: AddBusViewModel = viewModel()
) {
    val buses = listOf(
        Bus(
            "ônibus 1", "20 min", "R. John Harrison, 20",
            departureStop = "10",
            departureTime = "10",
            arrivalStop = "10",
            arrivalTime = "10",
            color = "#FF0000"   ,
            textColor = "#FFFFFF"
        )
    )

    // TODO: Fazer aparecer os onibus com base na proximidade por localização do user
//    val listBusResponse = viewModel.busList.value
    Column(modifier = Modifier.fillMaxSize()) {
        HeaderComponent(text = "Adicionar Notificação", hasBack = true)
        SearchBar(
            modifier = Modifier
                .height(90.dp)
                .padding(20.dp),
            shape = ShapeDefaults.Medium,
            query = "",
            onQueryChange = {},
            onSearch = {},
            active = false,
            onActiveChange = {},
            placeholder = { Text("Pesquisar") }
        ) {
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {
                items(buses) { bus ->
                    BusItemComponent(bus, true)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        BusItemLineComponent()

    }
}
