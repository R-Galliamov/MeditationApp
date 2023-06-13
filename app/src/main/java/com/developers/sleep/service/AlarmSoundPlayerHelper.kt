package com.developers.sleep.service

import android.app.Application
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.developers.sleep.GeneralPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


class AlarmSoundPlayerHelper @Inject constructor(
    private val application: Application,
) {
    private val mediaPlayer = MediaPlayer()

    private val generalSP =
        application.getSharedPreferences(GeneralPrefs.PREFS_NAME, Context.MODE_PRIVATE)
    private var volume = generalSP.getFloat(GeneralPrefs.VOLUME, 1f)

    private fun getAlarmSoundAssetFileDescriptor(alarmSoundFileName: String): AssetFileDescriptor {
        val assetFileDescriptor: AssetFileDescriptor =
            application.assets.openFd("alarmSounds/${alarmSoundFileName}")
        return assetFileDescriptor
    }

    fun playAlarmSound(alarmSoundFileName: String) {
        volume = generalSP.getFloat(GeneralPrefs.VOLUME, 1f)
        stopPlaying()
        val assetFileDescriptor: AssetFileDescriptor =
            getAlarmSoundAssetFileDescriptor(alarmSoundFileName)
        mediaPlayer.apply {
            setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )
            assetFileDescriptor.close()
            isLooping = true
            setVolume(volume, volume)
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            prepare()
            start()
        }
    }

    fun stopPlaying() {
        mediaPlayer.apply {
            if (isPlaying) {
                stop()
            }
            reset()
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AlarmSoundPlayerHelperModule {
    @Provides
    @Singleton
    fun provideAlarmSoundPlayerHelper(
        application: Application,
    ): AlarmSoundPlayerHelper {
        return AlarmSoundPlayerHelper(application)
    }
}
