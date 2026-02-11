package com.will.busnotification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.will.busnotification.data.model.Bus

@Composable
fun NotifiedBusComponent(
    bus: Bus,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC7DFFE)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top row: line name + time badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                // Left: bus icon + line name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "🚌", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = bus.lineShortName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }

                // Right: time badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2C3ED6))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = bus.arrivalTime,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Route: departure → arrival
            Text(
                text = "${bus.departureStop} → ${bus.arrivalStop}",
                fontSize = 14.sp,
                color = Color(0xFF1F2933),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Destination street
            Text(
                text = bus.destination,
                fontSize = 16.sp,
                color = Color(0xFF0F1720),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Bottom info: today's times
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hoje, ${bus.departureTime}",
                    fontSize = 12.sp,
                    color = Color(0xFF1F2933)
                )
                Text(
                    text = "Hoje, ${bus.arrivalTime}",
                    fontSize = 12.sp,
                    color = Color(0xFF1F2933)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotifiedBusComponentPreview() {
    NotifiedBusComponent(
        bus = Bus(
            lineName = "118Y",
            lineShortName = "118",
            destination = "Rua Willamy Ricardo Pinatto Andreotti",
            departureStop = "ponto 1",
            departureTime = "09:50",
            arrivalStop = "ponto 2",
            arrivalTime = "06:16",
            color = 0xFFC7DFFE.toInt(),
            textColor = 0xFF000000.toInt()
        )
    )
}