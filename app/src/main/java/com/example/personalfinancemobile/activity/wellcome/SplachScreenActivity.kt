package com.example.personalfinancemobile.activity.wellcome

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.LoginActivity
import com.example.personalfinancemobile.activity.Auth.RegisterActivity
import com.example.personalfinancemobile.app.Notif.NotificationReceiver
import com.example.personalfinancemobile.app.data.model.Auth.Constants
import com.example.personalfinancemobile.app.ui.utils.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplachScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach_screen)

        lifecycleScope.launch {
            delay(5000) // Tunda 5 detik (5000 ms)
            startActivity(Intent(this@SplachScreenActivity, LoginActivity::class.java))
            finish() // Tutup splash screen
        }


        // Panggil fungsi Notification
        NotificationHelper.createNotificationChannel(this)

        scheduleDailyNotification(
            this, 8, 0, 100,
            "Pagi Hari", "Jangan lupa catat transaksi pagi ini!"
        )
        scheduleDailyNotification(
            this, 13, 0, 101,
            "Siang Hari", "Jangan lupa catat transaksi pagi ini!"
        )
        scheduleDailyNotification(
            this, 20, 0, 102,
            "Malam Hari", "Jangan lupa catat transaksi Malam ini!"
        )

    }
    private fun scheduleDailyNotification(context: Context, jam: Int, menit: Int, requestCode: Int, title: String, message: String) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val calender = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, jam)
            set(Calendar.MINUTE, menit)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }
}
