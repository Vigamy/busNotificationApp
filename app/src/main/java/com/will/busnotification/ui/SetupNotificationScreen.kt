package com.will.busnotification.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.will.busnotification.data.model.NotificationWindow
import com.will.busnotification.ui.components.BusInfoCard
import com.will.busnotification.ui.components.HeaderComponent
import com.will.busnotification.ui.components.NotificationSettingsCard
import com.will.busnotification.ui.components.RemovePointButton
import com.will.busnotification.ui.components.SaveChangesButton
import com.will.busnotification.viewmodel.NotificationSetupPayload
import com.will.busnotification.viewmodel.NotificationSetupViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun SetupNotificationScreen(
    navController: NavHostController,
    lineCode: String?,
    lineName: String?,
    departureStop: String?,
    arrivalStop: String?,
    arrivalTime: String?
) {
    val notificationSetupViewModel: NotificationSetupViewModel = viewModel()

    val decodedLineName = lineName?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
    val decodedDepartureStop = departureStop?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
    val decodedArrivalStop = arrivalStop?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }

    var selectedWindow by remember { mutableStateOf(NotificationWindow(8, 0, 18, 0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HeaderComponent(
            text = "Configurar Notificação",
            hasBack = true,
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BusInfoCard(lineCode, decodedLineName, decodedDepartureStop, decodedArrivalStop, arrivalTime)
            Spacer(modifier = Modifier.height(16.dp))
            NotificationSettingsCard(
                initialWindow = selectedWindow,
                onNotificationWindowChanged = { selectedWindow = it }
            )
            Spacer(modifier = Modifier.weight(1f))
            SaveChangesButton(
                onClick = {
                    val payload = NotificationSetupPayload(
                        lineCode = lineCode.orEmpty(),
                        lineName = decodedLineName.orEmpty(),
                        departureStop = decodedDepartureStop.orEmpty(),
                        arrivalStop = decodedArrivalStop.orEmpty(),
                        notificationWindow = selectedWindow
                    )

                    notificationSetupViewModel.saveNotificationSetup(payload)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            RemovePointButton()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SetupNotificationScreenPreview() {
    SetupNotificationScreen(
        navController = rememberNavController(),
        lineCode = "118Y",
        lineName = "Rua Willamy Ricardo Pinatto Andreotti",
        departureStop = "ponto 1",
        arrivalStop = "ponto 2",
        arrivalTime = "06:16"
    )
}
