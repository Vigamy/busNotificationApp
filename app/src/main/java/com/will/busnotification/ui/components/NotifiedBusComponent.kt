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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFC7DFFE) // azul claro do card
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {

            // Badge do horário (canto superior direito)
            Box(
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = Color(0xFF2C3ED6), // azul escuro
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = bus.arrivalTime,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.padding(end = 72.dp) // espaço pro horário
            ) {

                // Linha + ícone
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Done, //Mudar depois pra Bus
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = bus.lineName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Origem → Destino
                Text(
                    text = "${bus.lineName} → ${bus.destination}",
                    fontSize = 14.sp,
                    color = Color(0xFF1F2933)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Nome do ponto
                Text(
                    text = bus.lineShortName,
                    fontSize = 14.sp,
                    color = Color(0xFF1F2933),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun NotifiedBusComponentPreview() {
    NotifiedBusComponent(bus = Bus("123", "Central", "10:00", "456", "Some Line", "nao sei", "10h", 123, 123))
}