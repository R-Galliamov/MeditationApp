package com.developers.sleep

import android.graphics.Color
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.dataModels.Playlist

object ColorConstants {
    val YANKEES_BLUE: Int = Color.parseColor("#FF21273C")
    val EERIE_BLACK: Int = Color.parseColor("#FF141926")

    val WHITE: Int = Color.parseColor("#FFFFFFFF")
    val BLUE_LIGHT: Int = Color.parseColor("#FF5777E4")
}

object GeneralPrefs {
    const val PREFS_NAME = "Prefs"
    const val IS_FIRST_LAUNCH = "isFirstLaunch"
    const val IS_GOAL_CHOSEN = "isGoalChosen"
}

object AlarmPrefs {
    const val PREFS_NAME = "AlarmPrefs"
    const val ALARM_TIME = "AlarmTime"
    const val IS_MUSIC_FOR_SLEEP_0N = "isMusicForSleepOn"
    const val STANDARD_ALARM_SOUND = "Sound 1"
    const val SELECTED_ALARM_MELODY_INDEX = "selectedAlarmMelodyIndex"
    const val SELECTED_ALARM_MELODY_NAME = "selectedAlarmMelodyName"
}

object AlarmPlayerPrefs {
    const val PREFS_NAME = "AlarmPrefs"
    const val SELECTED_MELODY = "selectedMelody"
    const val SELECTED_PLAYLIST_NAME = "selectedPlaylistName"
    const val MUSIC_DURATION = "musicDuration"
}

object PlayerPrefs {
    const val PREFS_NAME = "PlayerPrefs"
    const val SELECTED_MELODY = "selectedMelody"
    const val SELECTED_PLAYLIST = "selectedPlaylist"
    const val MUSIC_DURATION = "musicDuration"
}

object TestPrefs {
    const val PREFS_NAME = "userDataPrefs"
    const val USER_ANSWERS_COUNT = "userAnswersCount"
    const val ANSWER_PREFIX = "userAnswerOnQuestion"
}

object UserDataPrefs {
    const val PREFS_NAME = "userDataPrefs"
    const val IS_PREMIUM = "isPremium"
    const val NIGHTS_COUNT = "nightsCount"
}

const val EXTRA_ALARM_SOUND = "extraAlarmSound"
const val BASE_URL = "https://www.learningcontainer.com/wp-content/uploads/2020/02/" //TODO add url
const val PACKAGE_NAME = "com.developers.sleep" //TODO fix if changed
const val ACTION_ALARM_TRIGGERED = "actionAlarmTriggered"

object NotificationsConsts {
    const val ALARM_CHANNEL = "Alarm"
    const val TURN_OFF_ACTION = "TURN_OFF_ACTION"
}


val melody1 = Melody("Song 1", "file1.mp3")
val melody2 = Melody("Song 2", "file2.mp3")
val playlist2 = Playlist("Relaxation", listOf(melody2))
val melody3 = Melody("Nature", "file3.mp3")
val playlist3 = Playlist("Nature", listOf(melody3))
val playlist4 = Playlist("Delta waves", listOf(melody3))
val playlist5 = Playlist("Bedtime stories", listOf(melody3))
val playlist6 = Playlist("For kids", listOf(melody3))
val playlist7 = Playlist("ASMR", listOf(melody3))
val playlist8 = Playlist("Sleep", listOf(melody3))
val playlist1 = Playlist("Top", listOf(melody1, melody2, melody3))

val PLAYLIST_LIST = listOf(
    playlist1,
    playlist2,
    playlist3,
    playlist4,
    playlist5,
    playlist6,
    playlist7,
    playlist8
)
