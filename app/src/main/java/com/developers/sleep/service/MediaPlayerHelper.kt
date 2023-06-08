package com.developers.sleep.service

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


class MediaPlayerHelper @Inject constructor(
    private val application: Application,
) {
    private val mediaPlayer = MediaPlayer()
    private var _isMelodyPlaying = MutableLiveData<Boolean>()
    val isMelodyPlaying: LiveData<Boolean>
        get() = _isMelodyPlaying

    private var isAlarmPlaying = false
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var playbackJob: Job? = null
    private var elapsedMillis: Long = 0L
    private var melodyUrl: String? = null
    private var melodyDuration: Long = 0

    private fun getAlarmSoundAssetFileDescriptor(alarmSoundFileName: String): AssetFileDescriptor {
        val assetFileDescriptor: AssetFileDescriptor =
            application.assets.openFd("alarmSounds/${alarmSoundFileName}")
        return assetFileDescriptor
    }

    fun playAlarmSound(alarmSoundFileName: String) {
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
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            prepare()
            start()
        }
        isAlarmPlaying = mediaPlayer.isPlaying
    }

    fun setDuration(durationInMinutes: Int) {
        elapsedMillis = 0L
        melodyDuration = durationInMinutes.toLong() * 60 * 1000
    }

    fun playMelodyByUrl(melodyUrl: String) {
        this.melodyUrl = melodyUrl
        val duration = (melodyDuration - elapsedMillis).coerceAtLeast(0)
        stopPlaying()
        playbackJob = coroutineScope.launch {
            withContext(Dispatchers.IO) {
                mediaPlayer.apply {
                    setAudioAttributes(
                        AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    setDataSource(melodyUrl)
                    isLooping = true
                    prepare()
                }
            }
            mediaPlayer.seekTo(elapsedMillis.toInt())
            mediaPlayer.start()
            _isMelodyPlaying.value = mediaPlayer.isPlaying
            delay(duration)
            mediaPlayer.stop()
            _isMelodyPlaying.value = mediaPlayer.isPlaying
        }
    }

    fun resumePlaying() {
        if (melodyUrl != null) {
            playMelodyByUrl(melodyUrl!!)
        }
        _isMelodyPlaying.value = mediaPlayer.isPlaying
    }

    fun pausePlaying() {
        playbackJob?.cancel()
        elapsedMillis = mediaPlayer.currentPosition.toLong()
        mediaPlayer.pause()
        _isMelodyPlaying.value = mediaPlayer.isPlaying
    }

    fun stopPlaying() {
        mediaPlayer.apply {
            if (isPlaying || isAlarmPlaying) {
                stop()
            }
            reset()
        }
        _isMelodyPlaying.value = mediaPlayer.isPlaying
        isAlarmPlaying = mediaPlayer.isPlaying
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
