package com.developers.sleep.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.developers.sleep.ACTION_ALARM_TRIGGERED
import com.developers.sleep.EXTRA_ALARM_SOUND
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    @Inject
    lateinit var mediaPlayerHelper: AlarmSoundPlayerHelper

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("APP_LOG", "AlarmService started with ${intent?.getStringExtra(EXTRA_ALARM_SOUND)}")
        val alarmSoundFileName = intent?.getStringExtra(EXTRA_ALARM_SOUND)
        if (alarmSoundFileName != null) {
            mediaPlayerHelper.playAlarmSound(alarmSoundFileName)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerHelper.stopPlaying()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
