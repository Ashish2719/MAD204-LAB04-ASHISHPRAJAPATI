/**
 * Course: MAD204-LAB4
 * Name: Ashish Prajapati
 * Student ID : A00194842
 * Date: DECEMBER 6, 2025
 */
package com.example.lab4notesreminderapp.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast

/**
 * BroadcastReceiver that listens for CONNECTIVITY_CHANGE events[cite: 65].
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    /** Logs or Toasts when the network state changes[cite: 66]. */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

            // Log or Toast when the network state changes [cite: 66]
            if (isConnected) {
                Log.d("NetReceiver", "Network state changed: CONNECTED")
                Toast.makeText(context, "Network Connected!", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("NetReceiver", "Network state changed: DISCONNECTED")
                Toast.makeText(context, "Network Disconnected!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
