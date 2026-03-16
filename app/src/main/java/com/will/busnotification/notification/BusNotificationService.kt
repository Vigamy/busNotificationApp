package com.will.busnotification.notification

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalTime

/**
 * Foreground Service que monitora os ônibus salvos e dispara notificações.
 *
 * Roda durante a janela de horário configurada pelo usuário.
 * Faz polling da Routes API a cada 3 minutos.
 * Quando ETA ≤ 15 min, dispara notificação que atualiza a cada ~1 min.
 */
class BusNotificationService : Service() {

    companion object {
        private const val TAG = "BusNotifService"
        private const val POLLING_INTERVAL_MS = 3 * 60 * 1000L  // 3 minutos
        private const val UPDATE_INTERVAL_MS = 60 * 1000L       // 1 minuto (quando ETA ≤ 15min)
        private const val ETA_THRESHOLD_MINUTES = 15L
        private const val EXTRA_END_HOUR = "end_hour"
        private const val EXTRA_END_MINUTE = "end_minute"

        fun createIntent(context: Context, endHour: Int, endMinute: Int): Intent {
            return Intent(context, BusNotificationService::class.java).apply {
                putExtra(EXTRA_END_HOUR, endHour)
                putExtra(EXTRA_END_MINUTE, endMinute)
            }
        }
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val firestore = Firebase.firestore
    private var monitoringJob: Job? = null

    // Rastreia quais linhas já têm notificação ativa (lineCode → notificationId)
    private val activeNotifications = mutableMapOf<String, Int>()
    private var nextNotificationId = 2000

    private var endHour = 23
    private var endMinute = 59

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        endHour = intent?.getIntExtra(EXTRA_END_HOUR, 23) ?: 23
        endMinute = intent?.getIntExtra(EXTRA_END_MINUTE, 59) ?: 59

        Log.d(TAG, "Service iniciado. Janela termina às $endHour:$endMinute")

        // Inicia como Foreground Service
        startForeground(
            NotificationHelper.MONITORING_NOTIFICATION_ID,
            NotificationHelper.buildMonitoringNotification(this)
        )

        // Inicia o loop de monitoramento
        startMonitoring()

        return START_STICKY
    }

    private fun startMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = serviceScope.launch {
            while (isActive) {
                // Checa se ainda estamos dentro da janela
                val now = LocalTime.now()
                val endTime = LocalTime.of(endHour, endMinute)

                if (now.isAfter(endTime)) {
                    Log.d(TAG, "Fora da janela de notificação. Parando service.")
                    stopSelf()
                    return@launch
                }

                // Busca ônibus salvos no Firestore e checa ETA de cada um
                checkAllBuses()

                // Intervalo: se tem alguma notificação ativa (ETA ≤ 15min), poll mais rápido
                val interval = if (activeNotifications.isNotEmpty()) {
                    UPDATE_INTERVAL_MS
                } else {
                    POLLING_INTERVAL_MS
                }

                delay(interval)
            }
        }
    }

    private suspend fun checkAllBuses() {
        try {
            val snapshot = firestore.collection("locations").get().await()
            val documents = snapshot.documents

            if (documents.isEmpty()) {
                Log.d(TAG, "Nenhum ônibus salvo no Firestore")
                return
            }

            // Checa se estamos na janela de cada documento
            for (doc in documents) {
                val lineCode = doc.getString("lineCode") ?: doc.id
                val departureStop = doc.getString("departureStop") ?: ""
                val destination = doc.getString("destination") ?: ""
                val lineName = doc.getString("lineName") ?: lineCode

                // Checa janela específica desse ônibus
                val docStartHour = doc.getLong("startHour")?.toInt() ?: 0
                val docStartMinute = doc.getLong("startMinute")?.toInt() ?: 0
                val docEndHour = doc.getLong("endHour")?.toInt() ?: 23
                val docEndMinute = doc.getLong("endMinute")?.toInt() ?: 59

                val now = LocalTime.now()
                val windowStart = LocalTime.of(docStartHour, docStartMinute)
                val windowEnd = LocalTime.of(docEndHour, docEndMinute)

                if (now.isBefore(windowStart) || now.isAfter(windowEnd)) {
                    // Fora da janela desse ônibus — dismiss se tiver notificação ativa
                    activeNotifications[lineCode]?.let { notifId ->
                        NotificationHelper.dismissNotification(this@BusNotificationService, notifId)
                        activeNotifications.remove(lineCode)
                    }
                    continue
                }

                val result = BusArrivalChecker.checkArrival(
                    departureStop = departureStop,
                    destination = destination,
                    lineCode = lineCode,
                    lineName = lineName
                )

                if (result == null) continue

                handleArrivalResult(result)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao checar ônibus", e)
        }
    }

    private fun handleArrivalResult(result: BusArrivalResult) {
        val lineCode = result.lineCode

        when {
            // Ônibus já passou do ponto → dismiss notificação
            result.hasPassedStop -> {
                activeNotifications[lineCode]?.let { notifId ->
                    Log.d(TAG, "Ônibus $lineCode já passou. Removendo notificação.")
                    NotificationHelper.dismissNotification(this, notifId)
                    activeNotifications.remove(lineCode)
                }
            }

            // ETA ≤ 15 min → mostra/atualiza notificação
            result.minutesUntilArrival in 0..ETA_THRESHOLD_MINUTES -> {
                val notifId = activeNotifications.getOrPut(lineCode) { nextNotificationId++ }

                Log.d(TAG, "Ônibus $lineCode chega em ${result.minutesUntilArrival} min. " +
                        "Notificação ID: $notifId")

                NotificationHelper.showBusArrivalNotification(
                    context = this,
                    notificationId = notifId,
                    lineCode = lineCode,
                    lineName = result.lineName,
                    minutesUntilArrival = result.minutesUntilArrival,
                    departureStop = result.departureStop
                )
            }

            // ETA > 15 min → sem notificação ainda, mas mantém monitorando
            else -> {
                Log.d(TAG, "Ônibus $lineCode: ETA ${result.minutesUntilArrival} min (> ${ETA_THRESHOLD_MINUTES}min)")
            }
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "Service destruído. Limpando notificações.")
        monitoringJob?.cancel()
        serviceScope.cancel()

        // Limpa todas as notificações ativas
        activeNotifications.values.forEach { notifId ->
            NotificationHelper.dismissNotification(this, notifId)
        }
        activeNotifications.clear()

        super.onDestroy()
    }
}
