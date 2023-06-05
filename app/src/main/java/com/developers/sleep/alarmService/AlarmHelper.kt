package com.developers.sleep.alarmService

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.developers.sleep.Melody
import com.developers.sleep.EXTRA_ALARM_SOUND
import java.util.*

class AlarmHelper(private val context: Context) {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarmWithSound(time: Calendar, alarmSound: Melody) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_ALARM_SOUND, alarmSound.fileName)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
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
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }
}
