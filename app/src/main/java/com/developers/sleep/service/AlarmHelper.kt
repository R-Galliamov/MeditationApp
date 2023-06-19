package com.developers.sleep.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.developers.sleep.AlarmPrefs
import com.developers.sleep.EXTRA_ALARM_SOUND
import com.developers.sleep.dataModels.AlarmSound
import java.util.Calendar

class AlarmHelper(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(AlarmPrefs.PREFS_NAME, Context.MODE_PRIVATE)
    private var intentCode: Int = sharedPreferences.getInt(AlarmPrefs.ALARM_INTENT_CODE, 0)
        set(value) {
            field = value
            sharedPreferences.edit().putInt(AlarmPrefs.ALARM_INTENT_CODE, value).apply()
        }

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarmWithSound(time: Calendar, alarmSound: AlarmSound) {
        cancelAlarm() //cancel previous alarm
        intentCode++
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

    private fun cancelAlarm() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, intentCode, intent, PendingIntent.FLAG_MUTABLE)
        alarmManager.cancel(pendingIntent)
    }
}
