package com.developers.sleep.viewModel

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.MelodyRepository
import com.developers.sleep.PrefsConstants
import com.developers.sleep.alarmService.AlarmHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val application: Application,
    private val repository: MelodyRepository
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(PrefsConstants.ALARM_PREFS_NAME, 0)

    private val _alarmTime = MutableLiveData<Calendar>()
    val alarmTime: LiveData<Calendar>
        get() = _alarmTime

    private val _chosenAlarmSound = MutableLiveData<Melody>()
    val chosenAlarmSound: LiveData<Melody>
        get() = _chosenAlarmSound

    init {
        _chosenAlarmSound.value = repository.getChosenAlarmSound()

        val alarmMillis = sharedPreferences.getLong(PrefsConstants.ALARM_TIME, 0)
        if (alarmMillis != 0L) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = alarmMillis
            _alarmTime.value = calendar
        } else {
            val defaultTime = Calendar.getInstance()
            defaultTime.set(Calendar.HOUR_OF_DAY, 7)
            defaultTime.set(Calendar.MINUTE, 30)
            defaultTime.add(Calendar.DAY_OF_YEAR, 1)
            _alarmTime.value = defaultTime
        }
    }

    fun getAlarmSoundsList(): List<Melody> {
        return repository.alarmSoundsList
    }

    fun setChosenAlarmSound(alarmSound: Melody) {
        _chosenAlarmSound.value = alarmSound
        repository.saveChosenAlarmSound(alarmSound)
    }

    fun setAlarm() {
        val alarmTime = alarmTime.value
        if (alarmTime != null) {
            val alarmHelper = AlarmHelper(application)
            alarmHelper.setAlarmWithSound(alarmTime, chosenAlarmSound.value!!)
            Toast.makeText(application, "Alarm started", Toast.LENGTH_SHORT).show()
        }
    }

    fun setAlarmTime(time: Calendar) {
        time.add(Calendar.DAY_OF_YEAR, 1)

        //TODO remove in main
        val testTime = Calendar.getInstance()
        testTime.add(Calendar.MINUTE, 1)

        _alarmTime.value = testTime

        val editor = sharedPreferences.edit()
        editor.putLong(PrefsConstants.ALARM_TIME, time.timeInMillis)
        editor.apply()
    }

}
