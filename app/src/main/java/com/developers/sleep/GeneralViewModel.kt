package com.developers.sleep

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val repository: MelodyRepository
) : ViewModel() {

    //val selectedAlarmSound: LiveData<AlarmSound>

    fun playMelody(alarmSound: AlarmSound){
        repository.playMelody(alarmSound)
    }

    fun stopPlaying(){
        repository.stopPlaying()
    }

    fun getAlarmSoundsList(): List<AlarmSound>{
        return repository.alarmSoundsList
    }

    fun downloadMelody(fileName: String) {
        repository.downloadMelody(fileName)
    }

}
