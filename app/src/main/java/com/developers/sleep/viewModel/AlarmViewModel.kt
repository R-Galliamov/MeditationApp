package com.developers.sleep.viewModel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developers.sleep.PrefsConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(PrefsConstants.ALARM_PREFS_NAME, 0)

    private val _alarmTime = MutableLiveData<Calendar>()
    val alarmTime: LiveData<Calendar>
        get() = _alarmTime

    init {
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

    fun setAlarmTime(time: Calendar) {
        time.add(Calendar.DAY_OF_YEAR, 1)
        _alarmTime.value = time

        val editor = sharedPreferences.edit()
        editor.putLong(PrefsConstants.ALARM_TIME, time.timeInMillis)
        editor.apply()
    }

}
