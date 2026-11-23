package com.Faprire4.Kobmif.data.repository

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.data.local.CourtPreferences
import com.Faprire4.Kobmif.data.sample.SampleData
import com.Faprire4.Kobmif.domain.model.Chapter
import com.Faprire4.Kobmif.domain.model.CourtRecord
import com.Faprire4.Kobmif.domain.model.CourtSettings
import com.Faprire4.Kobmif.domain.model.Verdict
import com.Faprire4.Kobmif.domain.repository.CaseProgressRepository
import com.Faprire4.Kobmif.domain.repository.CourtSessionRepository
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CaseProgressRepositoryImpl(
    private val preferences: CourtPreferences
) : CaseProgressRepository {

    override fun record(): CourtRecord = CourtRecord(
        unlockedChapters = preferences.unlockedChapters(),
        casesSolved = preferences.casesSolved(),
        bestRanks = (0 until GameConfig.CHAPTER_COUNT).map { preferences.bestRank(it) }
    )

    override fun chapters(): List<Chapter> = SampleData.chapters

    override fun chapter(index: Int): Chapter = SampleData.chapterOf(index)

    override fun nextAttempt(chapterIndex: Int): Int = preferences.bumpAttempt(chapterIndex)

    override fun storeVerdict(verdict: Verdict) {
        preferences.writeOutcome(
            chapterIndex = verdict.chapterIndex,
            rank = verdict.ending.rank,
            solved = verdict.ending.victory
        )
    }

    override fun clearRecords() = preferences.wipeRecords()
}

class SettingsRepositoryImpl(
    private val preferences: CourtPreferences
) : SettingsRepository {

    override fun settings(): CourtSettings = CourtSettings(
        confirmAccusation = preferences.confirmAccusation(),
        candleMotion = preferences.candleMotion(),
        maskColours = preferences.maskColours()
    )

    override fun store(settings: CourtSettings) {
        preferences.writeSettings(
            confirm = settings.confirmAccusation,
            motion = settings.candleMotion,
            masks = settings.maskColours
        )
    }
}

class CourtSessionRepositoryImpl : CourtSessionRepository {

    private val state = MutableStateFlow<Verdict?>(null)

    override val verdict: StateFlow<Verdict?> = state.asStateFlow()

    override fun publish(value: Verdict) {
        state.value = value
    }
}
