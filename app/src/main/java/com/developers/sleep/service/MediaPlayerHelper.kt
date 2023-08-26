package com.developers.sleep.service

import android.app.Application
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developers.sleep.GeneralPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


class MediaPlayerHelper @Inject constructor(
    private val application: Application,
) {
    private val mediaPlayer = MediaPlayer()
    private var _isMelodyPlaying = MutableLiveData<Boolean>()
    val isMelodyPlaying: LiveData<Boolean>
        get() = _isMelodyPlaying

    //TODO create volume parameter in methods
    private val generalSP =
        application.getSharedPreferences(GeneralPrefs.PREFS_NAME, Context.MODE_PRIVATE)
    private var volume = generalSP.getFloat(GeneralPrefs.VOLUME, 1f)

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var playbackJob: Job? = null
    private var elapsedMillis: Long = 0L
    private var melodyUrl: String? = null
    private var melodyDurationMillis: Long = 0
    private var trackPositionMillis: Long = 0

    fun setDuration(durationInMinutes: Int) {
        elapsedMillis = 0L
        melodyDurationMillis = durationInMinutes.toLong() * 60 * 1000
    }

    fun resetTrackProgress() {
        trackPositionMillis = 0L
    }

    fun playMelodyByUrl(melodyUrl: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
            Log.d("App log", "Error")
            mediaPlayer.reset()
            playbackJob?.cancel()
            playbackJob = null
        }
        this.melodyUrl = melodyUrl
        volume = generalSP.getFloat(GeneralPrefs.VOLUME, 1f)
        stopPlaying()
        playbackJob = coroutineScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                mediaPlayer.apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
                    )
                    setDataSource(melodyUrl)
                    setVolume(volume, volume)
                    isLooping = true
                    prepare()
                }
            }
            mediaPlayer.seekTo(trackPositionMillis.toInt())
            mediaPlayer.start()
            _isMelodyPlaying.value = mediaPlayer.isPlaying

            while ((melodyDurationMillis - elapsedMillis) > 0) {
                if (isMelodyPlaying.value == true) {
                    elapsedMillis += 1000
                }
                delay(1000)
            }
            mediaPlayer.stop()
            _isMelodyPlaying.value = mediaPlayer.isPlaying
        }
    }

    fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
    }

    fun resumePlaying() {
        if (melodyUrl != null) {
            playMelodyByUrl(melodyUrl!!)
        }
        _isMelodyPlaying.value = mediaPlayer.isPlaying
    }

    fun pausePlaying() {
        if (mediaPlayer.isPlaying) {
            playbackJob?.cancel()
            trackPositionMillis = mediaPlayer.currentPosition.toLong()
            mediaPlayer.pause()
            _isMelodyPlaying.value = mediaPlayer.isPlaying
        }
    }

    fun stopPlaying() {
        mediaPlayer.apply {
            if (isPlaying) {
                stop()
            }
            reset()
        }
        _isMelodyPlaying.value = mediaPlayer.isPlaying
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
