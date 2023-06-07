package com.developers.sleep

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import androidx.core.net.toUri
import com.developers.sleep.dataModels.Melody
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AlarmRepository @Inject constructor(
    private val downloadManager: DownloadManager,
    private val application: Application
) {
    val alarmSharedPreferences: SharedPreferences =
        application.getSharedPreferences(AlarmPrefs.PREFS_NAME, Context.MODE_PRIVATE)

    val alarmSoundsList = getMp3SoundsFromAssets()

    val chosenAlarmSound = getChosenAlarmSoundName()


    fun getChosenAlarmSoundName(): String {
        val name = alarmSharedPreferences.getString(
            AlarmPrefs.SELECTED_ALARM_MELODY_NAME,
            AlarmPrefs.STANDARD_ALARM_SOUND
        ).toString()
        return name
    }

    fun getChosenAlarmSound(): Melody {
        return alarmSoundsList.first { it.name == chosenAlarmSound }
    }
    fun saveChosenAlarmSound(alarmSound: Melody) {
        val editor = alarmSharedPreferences.edit()
        editor.putString(AlarmPrefs.SELECTED_ALARM_MELODY_NAME, alarmSound.name)
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

    private fun getMp3SoundsFromAssets(): List<Melody> {
        val alarmSounds = mutableListOf<Melody>()
        try {
            val soundFiles = application.assets.list("alarmSounds") ?: emptyArray()
            var name = 1

            for (fileName in soundFiles) {
                alarmSounds.add(Melody("Sound $name", fileName))
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
object AlarmRepositoryModule {
    @Provides
    fun provideAlarmRepository(
        downloadManager: DownloadManager,
        application: Application
    ): AlarmRepository {
        return AlarmRepository(downloadManager, application)
    }
}
