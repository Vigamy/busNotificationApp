package com.will.busnotification.ui.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Requests location permission (ACCESS_FINE_LOCATION) at composition time.
 * Calls [onResult] with true/false when the user responds.
 *
 * This is designed to be placed once in the app entry point (e.g. HomeScreen)
 * so the permission dialog shows as soon as the user opens the app.
 */
@Composable
fun RequestLocationPermission(
    onResult: (granted: Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    val alreadyGranted = remember {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    if (alreadyGranted) {
        LaunchedEffect(Unit) { onResult(true) }
        return
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        onResult(fine || coarse)
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}

/**
 * Requests POST_NOTIFICATIONS permission (Android 13+).
 * On older versions, calls [onResult] with true immediately.
 *
 * This should NOT be called at app startup — only when the user actually needs notifications
 * (i.e., when saving their first bus notification).
 */
@Composable
fun RequestNotificationPermission(
    onResult: (granted: Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    // Pre-Tiramisu: notifications always allowed
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        LaunchedEffect(Unit) { onResult(true) }
        return
    }

    val alreadyGranted = remember {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    if (alreadyGranted) {
        LaunchedEffect(Unit) { onResult(true) }
        return
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        onResult(granted)
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

/**
 * Utility to check if notification permission is currently granted.
 */
fun isNotificationPermissionGranted(context: android.content.Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // Pre-13 doesn't need runtime permission
    }
}

/**
 * Utility to check if location permission is currently granted.
 */
fun isLocationPermissionGranted(context: android.content.Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
    ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}
