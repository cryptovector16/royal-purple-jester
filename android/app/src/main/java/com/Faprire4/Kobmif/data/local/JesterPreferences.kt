package com.Faprire4.Kobmif.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Faprire4.Kobmif.core.config.GameConfig

private const val STORE_NAME = "royal_purple_jester_prefs"

class JesterPreferences(context: Context) {

    private val store: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    var unlockedChapters: Int
        get() = store.getInt(KEY_UNLOCKED, 1)
        set(value) = store.edit().putInt(KEY_UNLOCKED, value).apply()

    var solvedChapters: Set<String>
        get() = store.getStringSet(KEY_SEALS, emptySet()) ?: emptySet()
        set(value) = store.edit().putStringSet(KEY_SEALS, value).apply()

    var chapterEndings: Set<String>
        get() = store.getStringSet(KEY_ENDINGS, emptySet()) ?: emptySet()
        set(value) = store.edit().putStringSet(KEY_ENDINGS, value).apply()

    var bestInsight: Int
        get() = store.getInt(KEY_BEST_INSIGHT, 0)
        set(value) = store.edit().putInt(KEY_BEST_INSIGHT, value).apply()

    var casesClosed: Int
        get() = store.getInt(KEY_CASES_CLOSED, 0)
        set(value) = store.edit().putInt(KEY_CASES_CLOSED, value).apply()

    var questionsPerCase: Int
        get() = store.getInt(KEY_QUESTIONS, GameConfig.QUESTIONS_COURT)
        set(value) = store.edit().putInt(KEY_QUESTIONS, value).apply()

    var sealHints: Boolean
        get() = store.getBoolean(KEY_SEAL_HINTS, true)
        set(value) = store.edit().putBoolean(KEY_SEAL_HINTS, value).apply()

    var steadyCandlelight: Boolean
        get() = store.getBoolean(KEY_STEADY, false)
        set(value) = store.edit().putBoolean(KEY_STEADY, value).apply()

    var autoFilterBoard: Boolean
        get() = store.getBoolean(KEY_AUTO_FILTER, false)
        set(value) = store.edit().putBoolean(KEY_AUTO_FILTER, value).apply()

    private companion object {
        const val KEY_UNLOCKED = "unlocked_chapter"
        const val KEY_SEALS = "chapter_seals"
        const val KEY_ENDINGS = "chapter_endings"
        const val KEY_BEST_INSIGHT = "best_insight"
        const val KEY_CASES_CLOSED = "cases_closed"
        const val KEY_QUESTIONS = "questions_per_case"
        const val KEY_SEAL_HINTS = "seal_hints"
        const val KEY_STEADY = "steady_candlelight"
        const val KEY_AUTO_FILTER = "auto_filter_board"
    }
}
