package com.developers.sleep

import android.graphics.Color
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.dataModels.Playlist
import com.developers.sleep.dataModels.Tip

object ColorConstants {
    val YANKEES_BLUE: Int = Color.parseColor("#FF21273C")
    val EERIE_BLACK: Int = Color.parseColor("#FF141926")

    val WHITE: Int = Color.parseColor("#FFFFFFFF")
    val BLUE_LIGHT: Int = Color.parseColor("#FF5777E4")
}

object PaywallConstants {
    const val WEEKLY_PAYMENT = 0
    const val YEAR_PAYMENT = 1
}

object GeneralPrefs {
    const val PREFS_NAME = "Prefs"
    const val IS_FIRST_LAUNCH = "isFirstLaunch"
    const val IS_GOAL_CHOSEN = "isGoalChosen"
    const val VOLUME = "volume"
}

object AlarmPrefs {
    const val PREFS_NAME = "AlarmPrefs"
    const val ALARM_TIME = "AlarmTime"
    const val IS_MUSIC_FOR_SLEEP_0N = "isMusicForSleepOn"
    const val STANDARD_ALARM_SOUND = "Dreamy Drizzle"
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
    const val NIGHTS_COUNT = "nightsCount"
    const val USER_NAME = "userName"
    const val USER_DATE = "userDate"
    const val USER_GENDER = "userGender"
    const val AVATAR_URI = "avatarUri"
}

const val EXTRA_ALARM_SOUND = "extraAlarmSound"
const val BASE_URL = "https://www.learningcontainer.com/wp-content/uploads/2020/02/" //TODO add url
const val PACKAGE_NAME = "com.developers.sleep" //TODO fix if changed
const val ACTION_ALARM_TRIGGERED = "actionAlarmTriggered"

object NotificationsConsts {
    const val ALARM_CHANNEL = "Alarm"
    const val TURN_OFF_ACTION = "TURN_OFF_ACTION"
}

val PLAYLIST_LIST = listOf(
    Playlist(
        "Relaxation",
        listOf(
            Melody("Tranquil Serenity", "https://meditationapp.b-cdn.net/music/Melody1.mp3", false),
            Melody("Inner Harmony", "https://meditationapp.b-cdn.net/music/Melody2.mp3", false),
            Melody("Zen Garden Whispers", "https://meditationapp.b-cdn.net/music/Melody3.mp3"),
            Melody("Stillness in Silence", "https://meditationapp.b-cdn.net/music/Melody4.mp3"),
            Melody("Soothing Breeze", "https://meditationapp.b-cdn.net/music/Melody5.mp3"),
            Melody("Calm Reflections", "https://meditationapp.b-cdn.net/music/Melody6.mp3"),
            Melody("Mystic Melodies", "https://meditationapp.b-cdn.net/music/Melody7.mp3")
        )
    ),
    Playlist(
        "Nature",
        listOf(
            Melody("Ethereal Bliss", "https://meditationapp.b-cdn.net/music/Melody8.mp3", false),
            Melody("Peaceful Oasis", "https://meditationapp.b-cdn.net/music/Melody9.mp3", false),
            Melody("Floating in Bliss", "https://meditationapp.b-cdn.net/music/Melody10.mp3"),
            Melody("Harmonious Journey", "https://meditationapp.b-cdn.net/music/Melody11.mp3"),
            Melody("Gentle Resonance", "https://meditationapp.b-cdn.net/music/Melody12.mp3"),
            Melody("Serenity Soundscape", "https://meditationapp.b-cdn.net/music/Melody13.mp3"),
            Melody("Mindful Melodies", "https://meditationapp.b-cdn.net/music/Melody14.mp3")
        )
    ),
    Playlist(
        "ASMR",
        listOf(
            Melody("Healing Harmonies", "https://meditationapp.b-cdn.net/music/Melody15.mp3", false),
            Melody("Melting into Peace", "https://meditationapp.b-cdn.net/music/Melody16.mp3", false),
            Melody("Divine Tranquility", "https://meditationapp.b-cdn.net/music/Melody17.mp3"),
            Melody("Nature's Lullaby", "https://meditationapp.b-cdn.net/music/Melody18.mp3"),
            Melody("Inner Balance", "https://meditationapp.b-cdn.net/music/Melody19.mp3"),
            Melody("Dreamy Reverie", "https://meditationapp.b-cdn.net/music/Melody20.mp3"),
            Melody("Soulful Stillness", "https://meditationapp.b-cdn.net/music/Melody21.mp3")
        )
    ),
    Playlist(
        "Delta waves",
        listOf(
            Melody("Enchanted Meditation", "https://meditationapp.b-cdn.net/music/Melody22.mp3", false),
            Melody("Celestial Harmony", "https://meditationapp.b-cdn.net/music/Melody23.mp3", false),
            Melody("Spiritual Serenade", "https://meditationapp.b-cdn.net/music/Melody24.mp3"),
            Melody("Silent Serenity", "https://meditationapp.b-cdn.net/music/Melody25.mp3"),
            Melody("Deep Relaxation", "https://meditationapp.b-cdn.net/music/Melody26.mp3"),
            Melody("Whispering Woods", "https://meditationapp.b-cdn.net/music/Melody27.mp3"),
            Melody("Guided Meditations", "https://meditationapp.b-cdn.net/music/Melody28.mp3")
        )
    ),
    Playlist(
        "Sleep",
        listOf(
            Melody("Blissful Awakening", "https://meditationapp.b-cdn.net/music/Melody29.mp3", false),
            Melody("Infinite Calm", "https://meditationapp.b-cdn.net/music/Melody30.mp3", false),
            Melody("Melodic Meditations", "https://meditationapp.b-cdn.net/music/Melody31.mp3"),
            Melody("Tranquil Tide", "https://meditationapp.b-cdn.net/music/Melody32.mp3"),
            Melody("Gentle Repose", "https://meditationapp.b-cdn.net/music/Melody33.mp3"),
            Melody("Harmony of the Heart", "https://meditationapp.b-cdn.net/music/Melody34.mp3"),
            Melody("Soothing Sanctuary", "https://meditationapp.b-cdn.net/music/Melody35.mp3")
        )
    )
)
