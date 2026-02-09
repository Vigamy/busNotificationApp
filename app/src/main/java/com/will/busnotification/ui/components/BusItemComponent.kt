package com.will.busnotification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.will.busnotification.data.model.Bus
import com.will.busnotification.util.computeMinutesUntil

@Composable
fun BusItemComponent(bus: Bus, hasArrow: Boolean,  modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFC7DFFE)
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${bus.lineShortName} - ${bus.destination}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Chegada: ${bus.arrivalTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(
                            color = Color(0xFF2E6FD6),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Passará em",
                            color = Color.White,
                            fontSize = 12.sp
                        )

                        val minutesText = computeMinutesUntil(bus.arrivalTime)

                        Text(
                            text = minutesText,
                            color = Color.White,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                if (hasArrow) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Abrir detalhes",
                        tint = Color(0xFF5B8FCF),
                        modifier = Modifier
                            .size(36.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun BusItemPreview() {
    BusItemComponent(
        bus = Bus(
            lineName = "Some Line Full",
            lineShortName = "42",
            destination = "Central",
            departureStop = "Stop A",
            departureTime = "10:00",
            arrivalStop = "Stop B",
            arrivalTime = "23:30",
            color = 0xFF2E6FD6.toInt(),
            textColor = 0xFFFFFFFF.toInt()
        ),
        hasArrow = true,
        modifier = Modifier.padding(8.dp)
    )
}
