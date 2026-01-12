package com.will.busnotification.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.will.busnotification.data.model.Bus
import com.will.busnotification.ui.components.HeaderComponent
import com.will.busnotification.ui.components.NotifiedBusComponent

@Preview(showBackground = true)
@Composable
fun NotificationHistoryScreen(
    navController: NavHostController = rememberNavController(),
) {

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderComponent(text = "Histórico de Notificações", hasBack = true, navController = navController)
        LazyColumn {
//            listOf(Bus())
        }
    }
}