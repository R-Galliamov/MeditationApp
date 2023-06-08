package com.developers.sleep.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developers.sleep.MelodyRepository
import com.developers.sleep.PLAYLIST_LIST
import com.developers.sleep.PlayerPrefs
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.dataModels.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _currentPlaylist = MutableLiveData<Playlist>()
    val currentPlaylist: LiveData<Playlist>
        get() = _currentPlaylist


    private val _musicDurationInMinutes = MutableLiveData<Int>()
    val musicDurationInMinutes: LiveData<Int>
        get() = _musicDurationInMinutes

    private val defaultPlayList = PLAYLIST_LIST[0] //TODO replace
    private val defaultMelody = defaultPlayList.melodiesList[0]

    init {
        _currentMelody.value = repository.getLastSavedMelody() ?: defaultMelody
        _currentPlaylist.value = repository.getLastSavedPlaylist() ?: defaultPlayList
        _musicDurationInMinutes.value = repository.getLastSavedDuration()
    }

    fun setMusicDurationInMinutes(duration: Int) {
        _musicDurationInMinutes.value = duration
        saveChanges()
    }

    fun setCurrentMelody(melody: Melody) {
        _currentMelody.value = melody
        saveChanges()
    }

    fun setCurrentPlaylist(playlist: Playlist) {
        _currentPlaylist.value = playlist
        saveChanges()
    }

    private fun saveChanges() {
        repository.saveCurrentPlayerSet(
            currentMelody.value ?: defaultMelody,
            currentPlaylist.value ?: defaultPlayList,
            musicDurationInMinutes.value ?: 30
        )
    }


}
