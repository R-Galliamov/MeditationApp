package com.developers.sleep.service

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.developers.sleep.EXTRA_ALARM_SOUND
import com.developers.sleep.NotificationsConsts
import com.developers.sleep.R
import com.developers.sleep.UserDataPrefs
import com.developers.sleep.viewModel.AlarmViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            NotificationsConsts.TURN_OFF_ACTION -> {
                val stopIntent = Intent(context, AlarmService::class.java)
                context.stopService(stopIntent)
            }

            else -> {
                val userSharedPreferences =
                    context.getSharedPreferences(UserDataPrefs.PREFS_NAME, Context.MODE_PRIVATE)

                val nightsCount = userSharedPreferences.getInt(UserDataPrefs.NIGHTS_COUNT, 1) + 1
                with(userSharedPreferences.edit()) {
                    putInt(UserDataPrefs.NIGHTS_COUNT, nightsCount)
                    apply()
                }

                val serviceIntent = Intent(context, AlarmService::class.java)
                Log.d("APP_LOG", "AlarmReceiver get ${intent?.getStringExtra(EXTRA_ALARM_SOUND)}")
                serviceIntent.putExtra(EXTRA_ALARM_SOUND, intent.getStringExtra(EXTRA_ALARM_SOUND))
                Log.d(
                    "APP_LOG",
                    "AlarmReceiver starts service with ${
                        serviceIntent?.getStringExtra(EXTRA_ALARM_SOUND)
                    }"
                )
                context.startService(serviceIntent)
                showNotification(context)
            }
        }
    }

    private fun showNotification(context: Context) {
        createNotificationChannel(context)
        val notification = buildNotification(context)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = NotificationsConsts.ALARM_CHANNEL
            val channelName = NotificationsConsts.ALARM_CHANNEL
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Alarm notification channel"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(context: Context): Notification {
        val channelId = NotificationsConsts.ALARM_CHANNEL
        val title = context.getString(R.string.alarm)
        val content = "Wake up!"

        val turnOffIntent = Intent(context, AlarmReceiver::class.java)
        turnOffIntent.action = NotificationsConsts.TURN_OFF_ACTION
        val turnOffPendingIntent = PendingIntent.getBroadcast(
            context, 0, turnOffIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val turnOffAction = NotificationCompat.Action.Builder(
            R.drawable.cross, context.getString(R.string.turn_off), turnOffPendingIntent
        ).build()

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.baseline_alarm)
            .addAction(turnOffAction)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDeleteIntent(turnOffPendingIntent)
            .build()
    }
}
