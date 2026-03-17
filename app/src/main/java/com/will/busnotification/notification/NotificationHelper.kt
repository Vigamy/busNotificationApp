package com.will.busnotification.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.will.busnotification.MainActivity
import com.will.busnotification.R

/**
 * Gerencia canais e criação de notificações do app.
 */
object NotificationHelper {

    const val CHANNEL_ID_BUS_ARRIVAL = "bus_arrival_channel"
    const val CHANNEL_ID_MONITORING = "bus_monitoring_channel"
    const val MONITORING_NOTIFICATION_ID = 1000

    /**
     * Cria os canais de notificação (deve ser chamado no Application.onCreate).
     */
    fun createNotificationChannels(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)

        val arrivalChannel = NotificationChannel(
            CHANNEL_ID_BUS_ARRIVAL,
            "Chegada de Ônibus",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificações de chegada de ônibus em tempo real"
            enableVibration(true)
        }

        val monitoringChannel = NotificationChannel(
            CHANNEL_ID_MONITORING,
            "Monitoramento Ativo",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notificação persistente enquanto o monitoramento está ativo"
            setShowBadge(false)
        }

        manager.createNotificationChannel(arrivalChannel)
        manager.createNotificationChannel(monitoringChannel)
    }

    /**
     * Cria a notificação persistente do Foreground Service.
     */
    fun buildMonitoringNotification(context: Context): android.app.Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID_MONITORING)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Monitorando ônibus")
            .setContentText("Verificando horários de chegada...")
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    /**
     * Cria ou atualiza uma notificação de chegada de ônibus.
     *
     * @param notificationId ID único por linha de ônibus
     * @param lineCode código da linha (ex: "118Y")
     * @param lineName nome da linha
     * @param minutesUntilArrival minutos restantes até a chegada
     * @param departureStop ponto de embarque
     */
    fun showBusArrivalNotification(
        context: Context,
        notificationId: Int,
        lineCode: String,
        lineName: String,
        minutesUntilArrival: Long,
        departureStop: String
    ) {
        val manager = context.getSystemService(NotificationManager::class.java)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, notificationId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val contentText = when {
            minutesUntilArrival <= 0 -> "🚌 Chegando agora no $departureStop!"
            minutesUntilArrival == 1L -> "🚌 Chega em 1 minuto no $departureStop"
            else -> "🚌 Chega em $minutesUntilArrival min no $departureStop"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_BUS_ARRIVAL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Ônibus $lineCode — $lineName")
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true) // Não vibrar a cada atualização
            .setOngoing(minutesUntilArrival > 0) // Dismiss automático quando chegar
            .setContentIntent(pendingIntent)
            .setAutoCancel(minutesUntilArrival <= 0)
            .build()

        manager.notify(notificationId, notification)
    }

    /**
     * Remove uma notificação de chegada.
     */
    fun dismissNotification(context: Context, notificationId: Int) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancel(notificationId)
    }
}
