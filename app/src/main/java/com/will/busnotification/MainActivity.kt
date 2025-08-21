package com.will.busnotification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.will.busnotification.ui.BusListScreen
import com.will.busnotification.ui.theme.BusNotificationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BusNotificationTheme {
                BusListScreen()
            }
        }
    }
}
