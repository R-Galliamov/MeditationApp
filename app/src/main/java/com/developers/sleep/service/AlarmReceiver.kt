package com.developers.sleep.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.developers.sleep.EXTRA_ALARM_SOUND
import com.developers.sleep.UserDataPrefs

class AlarmReceiver : BroadcastReceiver() {
    //TODO Check if new intent has not pass

    override fun onReceive(context: Context, intent: Intent) {
        val userSharedPreferences =
            context.getSharedPreferences(UserDataPrefs.PREFS_NAME, Context.MODE_PRIVATE)
        val nightsCount = userSharedPreferences.getInt(UserDataPrefs.NIGHTS_COUNT, 1)

        with(userSharedPreferences.edit()) {
            putInt(UserDataPrefs.NIGHTS_COUNT, nightsCount)
            apply()
        }

        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra(EXTRA_ALARM_SOUND, intent.getStringExtra(EXTRA_ALARM_SOUND))

        context.startService(serviceIntent)
    }
}
