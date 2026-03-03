package com.will.busnotification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun BusInfoCard(
    lineCode: String?,
    lineName: String?,
    departureStop: String?,
    arrivalStop: String?,
    arrivalTime: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE0EAFC))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = "Ícone de ônibus")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = lineCode ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                text = arrivalTime ?: "",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color(0xFF4C42E8), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        Column(modifier = Modifier.padding(top = 48.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(departureStop ?: "")
                Icon(Icons.Default.ArrowForward, contentDescription = "Seta para a direita")
                Text(arrivalStop ?: "")
            }
            Text(lineName ?: "", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsCard() {
    var selectedHour by remember { mutableStateOf(8) }
    var selectedMinute by remember { mutableStateOf(0) }
    var showTimePicker by remember { mutableStateOf(false) }

    val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE0EAFC))
            .clickable { showTimePicker = true }
            .padding(16.dp)
    ) {
        Text("Horário da notificação", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
        TimeSelector(time = selectedTime)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Toque no card para escolher o horário",
            style = MaterialTheme.typography.bodySmall
        )
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = true
        )

        TimePickerDialog(
            state = timePickerState,
            onConfirm = {
                selectedHour = timePickerState.hour
                selectedMinute = timePickerState.minute
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    state: TimePickerState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        text = {
            TimePicker(state = state)
        }
    )
}

@Composable
fun TimeSelector(time: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = time, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
fun SaveChangesButton() {
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B8FCF))
    ) {
        Text("Salvar alterações", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RemovePointButton() {
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
    ) {
        Text("Remover Ponto", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
