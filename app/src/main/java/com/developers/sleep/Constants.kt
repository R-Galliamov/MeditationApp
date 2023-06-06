package com.developers.sleep

import android.graphics.Color

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

object PlayerPrefs {
    const val PREFS_NAME = "PlayerPrefs"
    const val SELECTED_MELODY = "selectedMelody"
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
