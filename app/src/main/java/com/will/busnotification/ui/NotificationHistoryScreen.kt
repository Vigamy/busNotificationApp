package com.will.busnotification.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.will.busnotification.ui.components.HeaderComponent
import com.will.busnotification.ui.components.NotifiedBusComponent
import com.will.busnotification.viewmodel.BusViewModel

@Preview(showBackground = true)
@Composable
fun NotificationHistoryScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: BusViewModel = viewModel()
) {
    val buses by viewModel.notifiedBusList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNotifiedBuses()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderComponent(text = "Histórico de Notificações", hasBack = true)
        LazyColumn (
            modifier = Modifier.fillMaxSize()
                .padding(20.dp)
        ) {
            items(buses) { bus ->
                NotifiedBusComponent(bus)
            }
        }
    }
}