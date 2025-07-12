package com.example.personalfinancemobile.app.Notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.personalfinancemobile.app.ui.utils.NotificationHelper

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("title") ?: "Pengingat"
        val message = intent?.getStringExtra("message") ?: "Ayo buat transaksi"

        NotificationHelper.createNotificationChannel(context)
        NotificationHelper.showNotification(context, title, message)
    }
}