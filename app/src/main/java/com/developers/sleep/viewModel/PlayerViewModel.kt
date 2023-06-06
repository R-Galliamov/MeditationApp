package com.developers.sleep.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developers.sleep.AlarmPrefs
import com.developers.sleep.GeneralPrefs
import com.developers.sleep.MelodyRepository
import com.developers.sleep.PlayerPrefs
import com.developers.sleep.alarmService.AlarmHelper
import com.developers.sleep.dataModels.Melody
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val application: Application,
    private val repository: MelodyRepository
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(PlayerPrefs.PREFS_NAME, Context.MODE_PRIVATE)

    private val _currentMelody = MutableLiveData<Melody>()
    val currentMelody: LiveData<Melody>
        get() = _currentMelody

    fun setCurrentMelody(melody: Melody) {
        _currentMelody.value = melody
    }
}
