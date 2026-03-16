package com.will.busnotification.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Reagenda o worker de monitoramento após reboot do device.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device reiniciado. Reagendando monitoramento de ônibus.")
            NotificationSchedulerWorker.schedule(context)
        }
    }
}
