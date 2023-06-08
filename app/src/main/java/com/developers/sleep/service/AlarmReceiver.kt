package com.developers.sleep.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.developers.sleep.ACTION_ALARM_TRIGGERED
import com.developers.sleep.EXTRA_ALARM_SOUND
import com.developers.sleep.R
import com.developers.sleep.UserDataPrefs
import com.developers.sleep.ui.AppActivity
import com.developers.sleep.ui.SleepPlayerFragment

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val userSharedPreferences =
            context.getSharedPreferences(UserDataPrefs.PREFS_NAME, Context.MODE_PRIVATE)

        //TODO replace with repository
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
