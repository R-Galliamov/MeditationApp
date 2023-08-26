package com.developers.sleep.repository

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import com.developers.sleep.AlarmPrefs
import com.developers.sleep.dataModels.AlarmSound
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class AlarmSoundRepository @Inject constructor(
    private val application: Application
) {
    private val alarmSharedPreferences: SharedPreferences =
        application.getSharedPreferences(AlarmPrefs.PREFS_NAME, Context.MODE_PRIVATE)

    val alarmSoundsList = getMp3SoundsFromAssets()

    private val chosenAlarmSound = getChosenAlarmSoundName()


    private fun getChosenAlarmSoundName(): String {
        val name = alarmSharedPreferences.getString(
            AlarmPrefs.SELECTED_ALARM_MELODY_NAME, AlarmPrefs.STANDARD_ALARM_SOUND
        ).toString()
        return name
    }

    fun getChosenAlarmSound(): AlarmSound {
        return alarmSoundsList.first { it.name == chosenAlarmSound }
    }

    fun saveChosenAlarmSound(alarmSound: AlarmSound) {
        val editor = alarmSharedPreferences.edit()
        editor.putString(AlarmPrefs.SELECTED_ALARM_MELODY_NAME, alarmSound.name)
        editor.apply()
    }

    private fun getMp3SoundsFromAssets(): List<AlarmSound> {
        val alarmSounds = mutableListOf<AlarmSound>()
        val soundFiles = application.assets.list("alarmSounds") ?: emptyArray()

        for (file in soundFiles) {
            alarmSounds.add(AlarmSound(extractFileName(file), file))
        }
        return alarmSounds
    }

    private fun extractFileName(fileName: String): String {
        val extensionIndex = fileName.lastIndexOf('.')
        return if (extensionIndex != -1) {
            fileName.substring(0, extensionIndex)
        } else {
            fileName
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
object AlarmSoundRepositoryModule {
    @Provides
    fun provideAlarmSoundRepository(
        application: Application
    ): AlarmSoundRepository {
        return AlarmSoundRepository(application)
    }
}
