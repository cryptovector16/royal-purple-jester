package com.Faprire4.Kobmif.data.repository

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.data.local.JesterPreferences
import com.Faprire4.Kobmif.domain.model.CaseResult
import com.Faprire4.Kobmif.domain.model.Ending
import com.Faprire4.Kobmif.domain.model.Progress
import com.Faprire4.Kobmif.domain.repository.ProgressRepository

class ProgressRepositoryImpl(
    private val preferences: JesterPreferences
) : ProgressRepository {

    override fun read(): Progress {
        val solved = preferences.solvedChapters.mapNotNull { it.toIntOrNull() }.toSet()
        val endings = preferences.chapterEndings.mapNotNull { entry ->
            val parts = entry.split(":")
            if (parts.size != 2) {
                null
            } else {
                val index = parts[0].toIntOrNull()
                val ending = runCatching { Ending.valueOf(parts[1]) }.getOrNull()
                if (index == null || ending == null) null else index to ending
            }
        }.toMap()
        return Progress(
            unlockedChapters = preferences.unlockedChapters.coerceIn(1, GameConfig.CHAPTERS),
            solvedChapters = solved,
            endings = endings,
            bestInsight = preferences.bestInsight,
            casesClosed = preferences.casesClosed
        )
    }

    override fun record(result: CaseResult) {
        preferences.casesClosed = preferences.casesClosed + 1
        if (result.insight > preferences.bestInsight) {
            preferences.bestInsight = result.insight
        }
        val endings = preferences.chapterEndings
            .filterNot { it.startsWith("${result.chapterIndex}:") }
            .toMutableSet()
        endings.add("${result.chapterIndex}:${result.ending.name}")
        preferences.chapterEndings = endings
        if (result.correct) {
            val solved = preferences.solvedChapters.toMutableSet()
            solved.add(result.chapterIndex.toString())
            preferences.solvedChapters = solved
        }
        val unlocked = (result.chapterIndex + 2).coerceAtMost(GameConfig.CHAPTERS)
        if (unlocked > preferences.unlockedChapters) {
            preferences.unlockedChapters = unlocked
        }
    }
}
