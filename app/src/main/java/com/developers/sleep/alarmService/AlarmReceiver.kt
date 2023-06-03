package com.developers.sleep.alarmService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.developers.sleep.EXTRA_ALARM_SOUND

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra(EXTRA_ALARM_SOUND, intent.getStringExtra(EXTRA_ALARM_SOUND))
        context.startService(serviceIntent)
    }
}
