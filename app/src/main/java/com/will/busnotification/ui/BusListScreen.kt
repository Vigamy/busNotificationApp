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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.will.busnotification.ui.components.BusItem
import com.will.busnotification.viewmodel.BusViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
@Preview(showBackground = true)
fun BusListScreen(viewModel: BusViewModel = viewModel()) {
    val buses by viewModel.busList.collectAsState()
    val busname = "aaaaa"

    LaunchedEffect(Unit) {
        viewModel.loadBus()
    }

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
                .fillMaxSize()
                .padding(24.dp)
        ) {
            items(buses) { bus ->
                BusItem(bus)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}
