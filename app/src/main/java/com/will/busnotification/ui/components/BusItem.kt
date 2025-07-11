package com.will.busnotification.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.will.busnotification.data.model.Bus

@Composable
fun BusItem(bus: Bus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${bus.lineShortName} - ${bus.destination}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Chegada: ${bus.arrivalTime}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}