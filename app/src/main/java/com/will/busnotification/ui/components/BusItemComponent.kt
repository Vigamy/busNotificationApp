package com.will.busnotification.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.will.busnotification.data.model.Bus

@Composable
fun BusItemComponent(bus: Bus, hasArrow: Boolean,  modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFC7DFFE)
        ),
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "${bus.lineShortName} - ${bus.destination}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Chegada: ${bus.arrivalTime}", style = MaterialTheme.typography.bodyMedium)
            }
            if (hasArrow) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Bus Icon",
                    tint = Color(0xFF5B8FCF),
                    modifier = modifier
                        .align(Alignment.CenterEnd)
                        .size(48.dp)

                )
            }
        }
    }
}

@Preview
@Composable
fun BusItemPreview() {
    BusItemComponent(bus = Bus("123", "Central", "10:00", "456", "Some Line", "nao sei", "10h", 123, 123),
        hasArrow = true,
        modifier = Modifier.padding(8.dp)
    )
}
