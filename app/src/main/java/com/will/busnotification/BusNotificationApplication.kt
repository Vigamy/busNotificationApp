package com.will.busnotification

import android.app.Application
import com.will.busnotification.notification.NotificationHelper
import com.will.busnotification.notification.NotificationSchedulerWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BusNotificationApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Cria os canais de notificação
        NotificationHelper.createNotificationChannels(this)

        // Agenda o worker que monitora as janelas de notificação
        NotificationSchedulerWorker.schedule(this)
    }
}
