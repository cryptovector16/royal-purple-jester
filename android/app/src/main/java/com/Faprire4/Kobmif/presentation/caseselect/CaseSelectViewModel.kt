package com.Faprire4.Kobmif.presentation.caseselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.data.sample.SampleData
import com.Faprire4.Kobmif.domain.model.Chapter
import com.Faprire4.Kobmif.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CaseSelectViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CaseSelectUiState())
    val state: StateFlow<CaseSelectUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.read()
            val chapters = (0 until GameConfig.CHAPTERS).map { index ->
                Chapter(
                    index = index,
                    titleRes = SampleData.chapterTitles[index],
                    statementsPerClaimant = if (index >= 3) 4 else 3,
                    liars = if (index >= 2) 2 else 1,
                    unlocked = index < progress.unlockedChapters,
                    solved = progress.solvedChapters.contains(index),
                    ending = progress.endings[index]
                )
            }
            _state.update {
                it.copy(
                    chapters = chapters,
                    seals = progress.solvedChapters.size,
                    totalChapters = GameConfig.CHAPTERS
                )
            }
        }
    }
}
