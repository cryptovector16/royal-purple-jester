package com.Faprire4.Kobmif.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val seals: Int = 0,
    val totalChapters: Int = GameConfig.CHAPTERS,
    val bestInsight: Int = 0,
    val unlockedChapters: Int = 1
)

class GameOverViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.read()
            _state.update {
                it.copy(
                    seals = progress.solvedChapters.size,
                    bestInsight = progress.bestInsight,
                    unlockedChapters = progress.unlockedChapters
                )
            }
        }
    }
}
