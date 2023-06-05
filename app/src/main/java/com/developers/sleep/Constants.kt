package com.developers.sleep

import android.graphics.Color

object ColorConstants {
    val YANKEES_BLUE: Int = Color.parseColor("#FF21273C")
    val EERIE_BLACK: Int = Color.parseColor("#FF141926")

    val WHITE: Int = Color.parseColor("#FFFFFFFF")
    val BLUE_LIGHT: Int = Color.parseColor("#FF5777E4")
}

object PrefsConstants {
    const val PREFS_GENERAL_NAME = "Prefs"
    const val IS_FIRST_LAUNCH = "isFirstLaunch"
    const val IS_GOAL_CHOSEN = "isGoalChosen"
    const val SELECTED_ALARM_MELODY_INDEX = "selectedAlarmMelodyIndex"
    const val SELECTED_ALARM_MELODY_NAME = "selectedAlarmMelodyName"
    const val IS_MUSIC_FOR_SLEEP_0N = "isMusicForSleepOn"

    const val ALARM_PREFS_NAME = "AlarmPrefs"
    const val ALARM_TIME = "AlarmTime"

    const val STANDARD_ALARM_SOUND = "Sound 1"

    const val USER_DATA_PREFS_NAME = "userDataPrefs"
    const val USER_ANSWERS_COUNT = "userAnswersCount"
    const val ANSWER_PREFIX = "userAnswerOnQuestion"
}

const val EXTRA_ALARM_SOUND = "extraAlarmSound"
const val BASE_URL = "https://www.learningcontainer.com/wp-content/uploads/2020/02/" //TODO add url
const val PACKAGE_NAME = "com.developers.sleep" //TODO fix if changed
