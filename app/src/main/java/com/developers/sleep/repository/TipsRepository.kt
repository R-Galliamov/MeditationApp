package com.developers.sleep.repository

import android.content.Context
import com.developers.sleep.dataModels.Tip
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class TipsRepository {

    fun getTipsFromAssets(context: Context): List<Tip> {
        val tipList = mutableListOf<Tip>()

        try {
            val assetManager = context.assets
            val tipFiles = assetManager.list("tips") ?: arrayOf()

            for (tipFile in tipFiles) {
                if (tipFile.endsWith(".txt")) {
                    val inputStream = assetManager.open("tips/$tipFile")
                    val reader = BufferedReader(InputStreamReader(inputStream))

                    val name = reader.readLine()?.trim()
                    val contentBuilder = StringBuilder()

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        contentBuilder.append(line.orEmpty().trim())
                        contentBuilder.append("\n") // Add a line break if needed
                    }

                    reader.close()

                    if (name != null) {
                        val content = contentBuilder.toString().trim()
                        val tip = Tip(name, content)
                        tipList.add(tip)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return tipList
    }


}