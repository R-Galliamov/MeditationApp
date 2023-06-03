package com.developers.sleep

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


class MediaPlayerHelper @Inject constructor(
    private val application: Application,
) {
    private val mediaPlayer = MediaPlayer()

    fun getAlarmSoundAssetFileDescriptor(alarmSoundFileName: String): AssetFileDescriptor {
        val assetFileDescriptor: AssetFileDescriptor =
            application.assets.openFd("alarmSounds/${alarmSoundFileName}")
        return assetFileDescriptor
    }

    fun playLoopingAlarmSound(alarmSoundFileName: String) {
        stopPlaying()
        val assetFileDescriptor: AssetFileDescriptor =
            getAlarmSoundAssetFileDescriptor(alarmSoundFileName)
        mediaPlayer.setDataSource(
            assetFileDescriptor.fileDescriptor,
            assetFileDescriptor.startOffset,
            assetFileDescriptor.length
        )
        assetFileDescriptor.close()
        mediaPlayer.isLooping = true
        mediaPlayer.prepare()
        mediaPlayer.start()


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
object MediaPlayerHelperModule {
    @Provides
    @Singleton
    fun provideMediaPlayerHelper(
        application: Application,
    ): MediaPlayerHelper {
        return MediaPlayerHelper(application)
    }
}
