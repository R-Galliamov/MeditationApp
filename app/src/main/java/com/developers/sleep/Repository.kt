package com.developers.sleep

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Environment
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.io.IOException
import javax.inject.Inject

class MelodyRepository @Inject constructor(
    private val downloadManager: DownloadManager,
    private val application: Application
) {


    val alarmSharedPreferences: SharedPreferences =
        application.getSharedPreferences(PrefsConstants.ALARM_PREFS_NAME, Context.MODE_PRIVATE)

    val mediaPlayer = MediaPlayer()
    val alarmSoundsList = getMp3SoundsFromAssets()
    val alarmSoundsNames = getAlarmSoundFileNamesFromAssets()

    val chosenAlarmSound = getChosenAlarmSoundName()


    fun getChosenAlarmSoundName(): String {
        val name = alarmSharedPreferences.getString(
            PrefsConstants.SELECTED_ALARM_MELODY_NAME,
            PrefsConstants.STANDARD_ALARM_SOUND
        ).toString()
        return name
    }

    fun getChosenAlarmSound(): AlarmSound {
        return alarmSoundsList.first { it.name == chosenAlarmSound }
    }



    fun saveChosenAlarmSound(alarmSound: AlarmSound) {
        val editor = alarmSharedPreferences.edit()
        editor.putString(PrefsConstants.SELECTED_ALARM_MELODY_NAME, alarmSound.name)
        editor.apply()
    }

    private fun getAlarmSoundFileNamesFromAssets(): Array<String> {
        return try {
            application.assets.list("alarmSounds") ?: emptyArray()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyArray()
        }
    }

    private fun getMp3SoundsFromAssets(): List<AlarmSound> {
        val alarmSounds = mutableListOf<AlarmSound>()
        try {
            val soundFiles = application.assets.list("alarmSounds") ?: emptyArray()
            var name = 1

            for (fileName in soundFiles) {
                alarmSounds.add(AlarmSound("Sound $name", fileName))
                name++ //TODO Just for test
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // TODO Handle the exception
        }
        return alarmSounds
    }

    fun downloadMelody(fileName: String) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )
        if (!file.exists()) {
            val request = DownloadManager.Request((BASE_URL + fileName).toUri())
                .setMimeType("audio/mp3")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE) //TODO Hide it
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            downloadManager.enqueue(request)
        }
    }


}

@Module
@InstallIn(SingletonComponent::class)
object DownloadManagerModule {
    @Provides
    fun provideDownloadManager(@ApplicationContext appContext: Context): DownloadManager {
        return appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MelodyRepositoryModule {
    @Provides
    fun provideMelodyRepository(
        downloadManager: DownloadManager,
        application: Application
    ): MelodyRepository {
        return MelodyRepository(downloadManager, application)
    }
}
