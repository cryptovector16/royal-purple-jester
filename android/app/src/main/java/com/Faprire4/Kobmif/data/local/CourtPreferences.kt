package com.Faprire4.Kobmif.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Faprire4.Kobmif.core.config.GameConfig

class CourtPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun unlockedChapters(): Int =
        prefs.getInt(KEY_UNLOCKED, 1).coerceIn(1, GameConfig.CHAPTER_COUNT)

    fun casesSolved(): Int = prefs.getInt(KEY_SOLVED, 0)

    fun bestRank(chapterIndex: Int): Int = prefs.getInt(KEY_BEST + chapterIndex, -1)

    fun attempts(chapterIndex: Int): Int = prefs.getInt(KEY_ATTEMPT + chapterIndex, 0)

    fun bumpAttempt(chapterIndex: Int): Int {
        val next = attempts(chapterIndex) + 1
        prefs.edit().putInt(KEY_ATTEMPT + chapterIndex, next).apply()
        return next
    }

    fun writeOutcome(chapterIndex: Int, rank: Int, solved: Boolean) {
        val editor = prefs.edit()
        if (rank > bestRank(chapterIndex)) editor.putInt(KEY_BEST + chapterIndex, rank)
        if (solved) {
            if (bestRank(chapterIndex) < 2) editor.putInt(KEY_SOLVED, casesSolved() + 1)
            val unlocked = unlockedChapters()
            if (chapterIndex + 2 > unlocked) {
                editor.putInt(KEY_UNLOCKED, (chapterIndex + 2).coerceAtMost(GameConfig.CHAPTER_COUNT))
            }
        }
        editor.apply()
    }

    fun confirmAccusation(): Boolean = prefs.getBoolean(KEY_CONFIRM, true)

    fun candleMotion(): Boolean = prefs.getBoolean(KEY_MOTION, true)

    fun maskColours(): Boolean = prefs.getBoolean(KEY_MASKS, true)

    fun writeSettings(confirm: Boolean, motion: Boolean, masks: Boolean) {
        prefs.edit()
            .putBoolean(KEY_CONFIRM, confirm)
            .putBoolean(KEY_MOTION, motion)
            .putBoolean(KEY_MASKS, masks)
            .apply()
    }

    fun wipeRecords() {
        val editor = prefs.edit()
        editor.putInt(KEY_UNLOCKED, 1)
        editor.putInt(KEY_SOLVED, 0)
        for (index in 0 until GameConfig.CHAPTER_COUNT) {
            editor.remove(KEY_BEST + index)
            editor.remove(KEY_ATTEMPT + index)
        }
        editor.apply()
    }

    private companion object {
        const val STORE = "royal_purple_jester_court"
        const val KEY_UNLOCKED = "unlocked_chapters"
        const val KEY_SOLVED = "cases_solved"
        const val KEY_BEST = "best_rank_"
        const val KEY_ATTEMPT = "attempt_"
        const val KEY_CONFIRM = "confirm_accusation"
        const val KEY_MOTION = "candle_motion"
        const val KEY_MASKS = "mask_colours"
    }
}
