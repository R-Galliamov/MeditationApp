package com.developers.sleep.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.developers.sleep.EXTRA_ALARM_SOUND
import com.developers.sleep.dataModels.AlarmSound
import java.util.Calendar

class AlarmHelper(private val context: Context) {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarmWithSound(time: Calendar, alarmSound: AlarmSound) {
        val intent = Intent(context, AlarmReceiver::class.java)
        Log.d("APP_LOG", "alarmSound name to put is ${alarmSound.fileName}")
        intent.putExtra(EXTRA_ALARM_SOUND, alarmSound.fileName)
        Log.d("APP_LOG", "Intent created with ${intent?.getStringExtra(EXTRA_ALARM_SOUND)}")
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmSound.hashCode(),
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        alarmManager.cancel(pendingIntent)
    }
}
