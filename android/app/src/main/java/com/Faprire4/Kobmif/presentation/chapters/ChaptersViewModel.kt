package com.Faprire4.Kobmif.presentation.chapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.domain.model.Ending
import com.Faprire4.Kobmif.domain.repository.CaseProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChapterTileState(
    val index: Int,
    val unlocked: Boolean,
    val bestEnding: Ending?,
    val questions: Int,
    val maskedCount: Int
)

data class ChaptersUiState(
    val tiles: List<ChapterTileState> = emptyList(),
    val allSealed: Boolean = true
)

class ChaptersViewModel(
    private val progressRepository: CaseProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChaptersUiState())
    val state: StateFlow<ChaptersUiState> = _state.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            val record = progressRepository.record()
            val tiles = progressRepository.chapters().map { chapter ->
                ChapterTileState(
                    index = chapter.index,
                    unlocked = record.isUnlocked(chapter.index),
                    bestEnding = Ending.ofRank(record.bestRankOf(chapter.index)),
                    questions = chapter.questions,
                    maskedCount = chapter.maskedCount
                )
            }
            _state.value = ChaptersUiState(
                tiles = tiles,
                allSealed = tiles.count { it.unlocked } <= 1
            )
        }
    }
}
