package com.will.busnotification.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.will.busnotification.ui.components.BusItemComponent
import com.will.busnotification.viewmodel.BusViewModel

@Composable
@Preview(showBackground = true)
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: BusViewModel = viewModel()
) {
    val buses by viewModel.busList.collectAsState()
    val busname = "Bus Notification"
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadBus()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = busname,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(40.dp, 12.dp, 12.dp, 12.dp)

            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Faz a LazyColumn ocupar o espaço disponível
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {
                items(buses) { bus ->
                    BusItemComponent(bus)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                navController.navigate("addBuss")
            }
        ) {
            Icon(Icons.Filled.Add, "Add")
        }
    }
}
