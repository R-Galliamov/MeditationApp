package com.developers.sleep.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developers.sleep.AlarmPrefs
import com.developers.sleep.dataModels.AlarmSound
import com.developers.sleep.repository.AlarmSoundRepository
import com.developers.sleep.service.AlarmHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val application: Application,
    private val repository: AlarmSoundRepository
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(AlarmPrefs.PREFS_NAME, Context.MODE_PRIVATE)

    val _alarmTime = MutableLiveData<Calendar>()
    val alarmTime: LiveData<Calendar>
        get() = _alarmTime

    private val _chosenAlarmSound = MutableLiveData<AlarmSound>()
    val chosenAlarmSound: LiveData<AlarmSound>
        get() = _chosenAlarmSound

    init {
        _chosenAlarmSound.value = repository.getChosenAlarmSound()

        val alarmMillis = sharedPreferences.getLong(AlarmPrefs.ALARM_TIME, 0)
        if (alarmMillis != 0L) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = alarmMillis
            _alarmTime.value = calendar
        } else {
            val defaultTime = getDefaultAlarmTime()
            _alarmTime.value = defaultTime
        }
    }

    fun isMusicOn(): Boolean {
        return sharedPreferences.getBoolean(AlarmPrefs.IS_MUSIC_FOR_SLEEP_0N, true)
    }

    fun getAlarmSoundsList(): List<AlarmSound> {
        return repository.alarmSoundsList
    }

    fun setChosenAlarmSound(alarmSound: AlarmSound) {
        _chosenAlarmSound.value = alarmSound
        repository.saveChosenAlarmSound(alarmSound)
    }

    fun setAlarm() {
        val alarmTime = alarmTime.value
        if (alarmTime != null) {
            val alarmHelper = AlarmHelper(application)
            alarmHelper.setAlarmWithSound(alarmTime, chosenAlarmSound.value!!)
            Log.d("APP_LOG", "AlarmService started with ${chosenAlarmSound.value!!}")
        }
    }

    fun setAlarmTime(time: Calendar) {
        time.add(Calendar.DAY_OF_YEAR, 1)
        _alarmTime.value = time
        val editor = sharedPreferences.edit()
        editor.putLong(AlarmPrefs.ALARM_TIME, time.timeInMillis)
        editor.apply()
    }

    private fun getDefaultAlarmTime(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        return calendar
    }

}
