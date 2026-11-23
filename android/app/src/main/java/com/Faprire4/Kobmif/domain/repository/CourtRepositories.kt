package com.Faprire4.Kobmif.domain.repository

import com.Faprire4.Kobmif.domain.model.Chapter
import com.Faprire4.Kobmif.domain.model.CourtRecord
import com.Faprire4.Kobmif.domain.model.CourtSettings
import com.Faprire4.Kobmif.domain.model.Verdict
import kotlinx.coroutines.flow.StateFlow

interface CaseProgressRepository {
    fun record(): CourtRecord
    fun chapters(): List<Chapter>
    fun chapter(index: Int): Chapter
    fun nextAttempt(chapterIndex: Int): Int
    fun storeVerdict(verdict: Verdict)
    fun clearRecords()
}

interface SettingsRepository {
    fun settings(): CourtSettings
    fun store(settings: CourtSettings)
}

interface CourtSessionRepository {
    val verdict: StateFlow<Verdict?>
    fun publish(value: Verdict)
}
