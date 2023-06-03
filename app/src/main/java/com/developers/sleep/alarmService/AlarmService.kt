package com.developers.sleep.alarmService

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.developers.sleep.AlarmSound
import com.developers.sleep.EXTRA_ALARM_SOUND
import com.developers.sleep.MediaPlayerHelper
import com.developers.sleep.MelodyRepository
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService: Service() {

    override fun onCreate() {
        super.onCreate()
    }

    @Inject
    private lateinit var mediaPlayerHelper: MediaPlayerHelper

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("APP_LOG", "AlarmService started")
        val alarmSoundFileName = intent?.getStringExtra(EXTRA_ALARM_SOUND)
        if (alarmSoundFileName != null) {
            mediaPlayerHelper.playLoopingAlarmSound(alarmSoundFileName)
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
