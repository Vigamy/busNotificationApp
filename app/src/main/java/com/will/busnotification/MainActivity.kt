package com.will.busnotification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.will.busnotification.data.api.GoogleApiInstance
import com.will.busnotification.data.api.GoogleApiInterface
import com.will.busnotification.navigation.AppNavHost
import com.will.busnotification.ui.HomeScreen
import com.will.busnotification.ui.theme.BusNotificationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppNavHost()
//            BusNotificationTheme {
//                HomeScreen()
//            }
        }
    }
}
