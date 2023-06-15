package com.developers.sleep.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developers.sleep.PLAYLIST_LIST
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.dataModels.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val application: Application,
) : AndroidViewModel(application) {

    val playlistsList = PLAYLIST_LIST

    private val _currentMelody = MutableLiveData<Melody>()
    val currentMelody: LiveData<Melody>
        get() = _currentMelody

    private val _currentPlaylist = MutableLiveData<Playlist>()
    val currentPlaylist: LiveData<Playlist>
        get() = _currentPlaylist


    private val _musicDurationInMinutes = MutableLiveData<Int>()
    val musicDurationInMinutes: LiveData<Int>
        get() = _musicDurationInMinutes

    private val defaultPlayList = PLAYLIST_LIST[0]
    private val defaultMelody = defaultPlayList.melodiesList[0]

    init {
        _currentMelody.value = defaultMelody
        _currentPlaylist.value = defaultPlayList
        _musicDurationInMinutes.value = 30
    }

    fun setMusicDurationInMinutes(duration: Int) {
        _musicDurationInMinutes.value = duration
    }

    fun setCurrentMelody(melody: Melody) {
        _currentMelody.value = melody
    }

    fun setCurrentPlaylist(playlist: Playlist) {
        _currentPlaylist.value = playlist
    }
}
