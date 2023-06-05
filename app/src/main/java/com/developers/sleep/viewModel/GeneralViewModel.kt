package com.developers.sleep.viewModel

import androidx.lifecycle.ViewModel
import com.developers.sleep.Melody
import com.developers.sleep.MelodyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val repository: MelodyRepository
) : ViewModel() {


    fun getAlarmSoundsList(): List<Melody>{
        return repository.alarmSoundsList
    }

    fun downloadMelody(fileName: String) {
        repository.downloadMelody(fileName)
    }

}
