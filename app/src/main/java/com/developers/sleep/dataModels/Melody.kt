package com.developers.sleep.dataModels

data class Melody(
    val name: String,
    val url: String,
    val isPremium: Boolean = true,
)

data class AlarmSound(
    val name: String,
    val fileName: String,
)

data class Playlist(
    val name: String,
    val melodiesList: List<Melody>,
)


