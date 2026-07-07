package com.example.project_uas.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.project_uas.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val namaObat = intent.getStringExtra("NAMA_OBAT") ?: "Obat"
        val id = intent.getIntExtra("ID_PENGINGAT", 0)

        // 1. Tampilkan Notifikasi
        showNotification(context, id, "Waktunya Minum Obat", "Ayo minum obat: $namaObat")

        // 2. Bunyikan Suara Alarm
        playAlarmSound(context)
    }

    private fun showNotification(context: Context, id: Int, title: String, message: String) {
        val channelId = "alarm_obat_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Karena minSdk 26, NotificationChannel langsung dibuat tanpa cek versi
        val channel = NotificationChannel(channelId, "Pengingat Obat", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(id, builder.build())
    }

    private fun playAlarmSound(context: Context) {
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            RingtoneManager.getRingtone(context, alarmUri).play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}