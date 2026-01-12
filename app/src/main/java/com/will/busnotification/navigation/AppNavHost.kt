package com.will.busnotification.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.will.busnotification.ui.AddBusScreen
import com.will.busnotification.ui.HomeScreen
import com.will.busnotification.ui.NotificationHistoryScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("addBuss") { AddBusScreen(navController) }
        composable("notificationHistory") { NotificationHistoryScreen(navController) }
    }
}