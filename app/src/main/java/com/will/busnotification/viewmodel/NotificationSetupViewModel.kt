package com.will.busnotification.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.will.busnotification.data.model.NotificationWindow

data class NotificationSetupPayload(
    val lineCode: String,
    val lineName: String,
    val departureStop: String,
    val arrivalStop: String,
    val notificationWindow: NotificationWindow
)

interface NotificationScheduleManager {
    fun scheduleNotificationWindow(payload: NotificationSetupPayload)
}

class DefaultNotificationScheduleManager : NotificationScheduleManager {
    override fun scheduleNotificationWindow(payload: NotificationSetupPayload) {
        // TODO: substituir por integração real com WorkManager para agendamento recorrente.
        Log.d("NotificationSchedule", "Agendando janela: $payload")
    }
}

class NotificationSetupViewModel : ViewModel() {

    private val firestore = Firebase.firestore
    private val scheduleManager: NotificationScheduleManager = DefaultNotificationScheduleManager()

    fun saveNotificationSetup(
        payload: NotificationSetupPayload,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        val firestorePayload = mapOf(
            "lineCode" to payload.lineCode,
            "lineName" to payload.lineName,
            "departureStop" to payload.departureStop,
            "arrivalStop" to payload.arrivalStop,
            "startHour" to payload.notificationWindow.startHour,
            "startMinute" to payload.notificationWindow.startMinute,
            "endHour" to payload.notificationWindow.endHour,
            "endMinute" to payload.notificationWindow.endMinute,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("notification_setups")
            .add(firestorePayload)
            .addOnSuccessListener {
                scheduleManager.scheduleNotificationWindow(payload)
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error)
            }
    }
}

