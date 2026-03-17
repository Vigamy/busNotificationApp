package com.will.busnotification.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.will.busnotification.data.dto.NotificationSetupPayload
import com.will.busnotification.notification.NotificationSchedulerWorker

class NotificationSetupViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    fun saveNotificationSetup(
        context: Context,
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
            "destination" to payload.destination,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("locations").document(firestorePayload["lineCode"].toString())
            .set(firestorePayload)
            .addOnSuccessListener {
                // Reagenda o worker pra considerar a nova janela imediatamente
                NotificationSchedulerWorker.schedule(context)

                onSuccess()
                Log.d("NotificationSetupVM", "Setup salvo e worker reagendado")
            }
            .addOnFailureListener { error ->
                onError(error)
                Log.e("NotificationSetupVM", "Falha ao salvar: $error")
            }
    }
}
