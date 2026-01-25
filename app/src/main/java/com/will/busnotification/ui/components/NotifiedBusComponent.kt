package com.will.busnotification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
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
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFC7DFFE) // light blue background
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {

            // Top-center small avatar badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-12).dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = bus.lineShortName.takeIf { it.isNotEmpty() }?.firstOrNull()?.toString() ?: "B",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3ED6)
                )
            }

            // Time badge (top-right)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2C3ED6))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = bus.arrivalTime,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left icon circle
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    // Use a simple emoji as a fallback for the bus icon to avoid requiring
                    // the material-icons-extended dependency in the project.
                    Text(
                        text = "🚌",
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    // Large line name
                    Text(
                        text = bus.lineName,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Route (departure -> destination)
                    Text(
                        text = "${bus.departureStop} → ${bus.arrivalStop}",
                        fontSize = 16.sp,
                        color = Color(0xFF1F2933),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Large address / street line at bottom
            Text(
                text = bus.destination,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(top = 8.dp),
                fontSize = 18.sp,
                color = Color(0xFF0F1720),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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