package com.will.busnotification.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.will.busnotification.ui.AddNotificationScreen
import com.will.busnotification.ui.HomeScreen
import com.will.busnotification.ui.NotificationHistoryScreen
import com.will.busnotification.ui.SetupNotificationScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("addBuss") { AddNotificationScreen(navController) }
        composable("notificationHistory") { NotificationHistoryScreen(navController) }
        composable(
            "notificationSetup/{lineCode}/{lineName}/{departureStop}/{arrivalStop}/{arrivalTime}",
            arguments = listOf(
                navArgument("lineCode") { type = NavType.StringType },
                navArgument("lineName") { type = NavType.StringType },
                navArgument("departureStop") { type = NavType.StringType },
                navArgument("arrivalStop") { type = NavType.StringType },
                navArgument("arrivalTime") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            SetupNotificationScreen(
                navController = navController,
                lineCode = arguments?.getString("lineCode"),
                lineName = arguments?.getString("lineName"),
                departureStop = arguments?.getString("departureStop"),
                arrivalStop = arguments?.getString("arrivalStop"),
                arrivalTime = arguments?.getString("arrivalTime")
            )
        }
    }
}