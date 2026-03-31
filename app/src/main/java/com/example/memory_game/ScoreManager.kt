package com.example.memory_game

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.memory_game.ui.screens.ScoreEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScoreManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("scores_pref", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveScore(name: String, score: Int) {
        val scores = getScores().toMutableList()
        scores.add(ScoreEntry(name, score))
        val sortedScores = scores.sortedByDescending { it.score }.take(10)
        prefs.edit {
            putString("scores_list", gson.toJson(sortedScores))
        }
    }

    fun getScores(): List<ScoreEntry> {
        val json = prefs.getString("scores_list", null) ?: return emptyList()
        val type = object : TypeToken<List<ScoreEntry>>() {}.type
        return gson.fromJson(json, type)
    }
}
