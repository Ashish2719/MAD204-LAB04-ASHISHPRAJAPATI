/**
 * Course: MAD204-LAB4
 * Name: Ashish Prajapati
 * Student ID : A00194842
 * Date: DECEMBER 6, 2025
 */

package com.example.lab4notesreminderapp.utility

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lab4notesreminderapp.R

/**
 * Service to handle background tasks for note reminders.
 */
class ReminderService : Service() {

    // Fixed naming convention (const val for compile-time constants)
    private val CHANNEL_ID = "reminder_channel"
    private  val TAG = "ReminderService"

    override fun onBind(intent: Intent?): IBinder? {
        return null // Services that don't allow binding return null
    }

    /** When started, the service waits 5 seconds and then posts a notification. */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started. Waiting 5 seconds for reminder...")

        // Use Handler to delay the task by 5000 milliseconds (5 seconds)
        Handler(Looper.getMainLooper()).postDelayed({
            showNotification()
            Log.d(TAG, "Notification posted. Service stopping itself.")
            stopSelf() // Stop the service when the task is done
        }, 5000)

        return START_STICKY
    }

    /** Builds and displays the "Check your notes!" notification. (Part E) */
    private fun showNotification() {
        val notificationManager = NotificationManagerCompat.from(this)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert) // FIX: Using a safe system icon
            .setContentTitle("Reminder") // Title: "Reminder"
            .setContentText("Check your notes!") // Text: "Check your notes!"
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(101, notification)
    }
}
