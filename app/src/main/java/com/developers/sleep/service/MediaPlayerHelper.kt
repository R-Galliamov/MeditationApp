package com.developers.sleep.service

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

    private var _isPlaying = false

    val isPlaying: Boolean
        get() = _isPlaying

    fun getAlarmSoundAssetFileDescriptor(alarmSoundFileName: String): AssetFileDescriptor {
        val assetFileDescriptor: AssetFileDescriptor =
            application.assets.openFd("alarmSounds/${alarmSoundFileName}")
        return assetFileDescriptor
    }

    fun startPlayMelodyFromInternet(melodyUrl: String) {
        stopPlaying()
        mediaPlayer.apply {
            setDataSource(melodyUrl)
            prepareAsync()
            setOnPreparedListener { mp ->
                // Start playing the melody once it's prepared
                mp.start()
            }
        }
    }

    fun startPlayLoopingAlarmSound(alarmSoundFileName: String) {
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
        _isPlaying = mediaPlayer.isPlaying
    }

    fun continuePlaying() {
        mediaPlayer.apply {
            mediaPlayer.start()
        }
        _isPlaying = true
    }

    fun pausePlaying() {
        mediaPlayer.apply {
            if (isPlaying) {
                pause()
            }
            _isPlaying = mediaPlayer.isPlaying
        }
    }

    fun stopPlaying() {
        mediaPlayer.apply {
            if (isPlaying) {
                stop()
            }
            reset()
        }
        _isPlaying = mediaPlayer.isPlaying
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
