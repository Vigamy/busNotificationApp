package com.will.busnotification.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * WorkManager worker que verifica periodicamente se estamos dentro de alguma
 * janela de notificação e inicia/para o ForegroundService.
 *
 * Roda a cada 15 minutos (mínimo do WorkManager) e checa se algum ônibus
 * salvo tem uma janela ativa agora.
 */
class NotificationSchedulerWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "NotifSchedulerWorker"
        private const val WORK_NAME = "bus_notification_scheduler"

        /**
         * Agenda o worker periódico que checa as janelas de notificação.
         */
        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<NotificationSchedulerWorker>(
                15, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )

            Log.d(TAG, "Worker periódico agendado (a cada 15 min)")
        }

        /**
         * Cancela o worker periódico.
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
            Log.d(TAG, "Worker periódico cancelado")
        }
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Worker executando...")

        return try {
            val firestore = Firebase.firestore
            val snapshot = firestore.collection("locations").get().await()
            val documents = snapshot.documents

            if (documents.isEmpty()) {
                Log.d(TAG, "Nenhum ônibus salvo. Nada pra monitorar.")
                return Result.success()
            }

            val now = LocalTime.now()
            var shouldStartService = false
            var latestEndHour = 0
            var latestEndMinute = 0

            for (doc in documents) {
                val startHour = doc.getLong("startHour")?.toInt() ?: 0
                val startMinute = doc.getLong("startMinute")?.toInt() ?: 0
                val endHour = doc.getLong("endHour")?.toInt() ?: 23
                val endMinute = doc.getLong("endMinute")?.toInt() ?: 59

                val windowStart = LocalTime.of(startHour, startMinute)
                val windowEnd = LocalTime.of(endHour, endMinute)

                if (!now.isBefore(windowStart) && !now.isAfter(windowEnd)) {
                    shouldStartService = true
                    // Pega o horário de fim mais tardio entre todas as janelas ativas
                    if (endHour > latestEndHour || (endHour == latestEndHour && endMinute > latestEndMinute)) {
                        latestEndHour = endHour
                        latestEndMinute = endMinute
                    }
                }
            }

            if (shouldStartService) {
                Log.d(TAG, "Dentro de janela ativa. Iniciando ForegroundService (fim: $latestEndHour:$latestEndMinute)")
                val intent = BusNotificationService.createIntent(
                    applicationContext,
                    latestEndHour,
                    latestEndMinute
                )
                applicationContext.startForegroundService(intent)
            } else {
                Log.d(TAG, "Fora de todas as janelas. Service não será iniciado.")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Erro no worker", e)
            Result.retry()
        }
    }
}
