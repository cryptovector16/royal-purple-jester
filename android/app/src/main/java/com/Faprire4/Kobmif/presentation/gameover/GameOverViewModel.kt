package com.Faprire4.Kobmif.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.Verdict
import com.Faprire4.Kobmif.domain.repository.CaseProgressRepository
import com.Faprire4.Kobmif.domain.repository.CourtSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class GameOverUiState(
    val verdict: Verdict? = null,
    val nextChapterIndex: Int = -1
)

class GameOverViewModel(
    sessionRepository: CourtSessionRepository,
    private val progressRepository: CaseProgressRepository
) : ViewModel() {

    val state: StateFlow<GameOverUiState> = sessionRepository.verdict
        .map { verdict -> GameOverUiState(verdict = verdict, nextChapterIndex = nextOf(verdict)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameOverUiState())

    private fun nextOf(verdict: Verdict?): Int {
        if (verdict == null || !verdict.ending.victory) return -1
        val candidate = verdict.chapterIndex + 1
        if (candidate >= GameConfig.CHAPTER_COUNT) return -1
        return if (progressRepository.record().isUnlocked(candidate)) candidate else -1
    }
}
