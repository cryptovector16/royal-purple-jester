package com.Faprire4.Kobmif.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.read()
            _state.update {
                it.copy(
                    casesClosed = progress.casesClosed,
                    bestInsight = progress.bestInsight,
                    seals = progress.solvedChapters.size,
                    totalChapters = GameConfig.CHAPTERS,
                    nextChapterIndex = (progress.unlockedChapters - 1).coerceIn(0, GameConfig.CHAPTERS - 1)
                )
            }
        }
    }
}
