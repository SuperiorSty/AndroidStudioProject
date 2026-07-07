package com.example.project_uas.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.project_uas.modelData.Pengingat
import com.example.project_uas.receiver.AlarmReceiver
import java.util.Calendar

object AlarmHelper {

    // Fungsi singkat untuk sinkronisasi semua data
    fun syncAlarms(context: Context, list: List<Pengingat>) {
        list.forEach {
            if (it.status_aktif == 1) setAlarm(context, it)
            else cancelAlarm(context, it.id)
        }
    }

    fun setAlarm(context: Context, pengingat: Pengingat) {
        if (pengingat.id == 0) return

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ID_PENGINGAT", pengingat.id)
            putExtra("NAMA_OBAT", pengingat.nama_obat ?: "Obat")
        }

        // FLAG_IMMUTABLE wajib untuk Android baru, aman di minSdk 26
        val pi = PendingIntent.getBroadcast(context, pengingat.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val p = pengingat.waktu_minum.split(":")
        if (p.size < 2) return

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, p[0].toInt())
            set(Calendar.MINUTE, p[1].toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DATE, 1)
        }

        try {
            // Android 12+ (API 31) butuh penanganan khusus untuk alarm presisi
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            } else {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            }
        } catch (e: Exception) {
            Log.e("Alarm", "Gagal: ${e.message}")
        }
    }

    fun cancelAlarm(context: Context, id: Int) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        // Cari alarm yang aktif berdasarkan ID, lalu batalkan
        PendingIntent.getBroadcast(context, id, intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)?.let {
            am.cancel(it)
            it.cancel()
        }
    }
}