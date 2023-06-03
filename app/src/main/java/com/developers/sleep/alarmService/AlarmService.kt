package com.developers.sleep.alarmService

import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.IBinder
import com.developers.sleep.AlarmSound
import com.developers.sleep.EXTRA_ALARM_SOUND
import com.developers.sleep.MediaPlayerHelper
import com.developers.sleep.MelodyRepository
import java.io.IOException
import javax.inject.Inject


class AlarmService @Inject constructor(private val mediaPlayerHelper: MediaPlayerHelper): Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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
