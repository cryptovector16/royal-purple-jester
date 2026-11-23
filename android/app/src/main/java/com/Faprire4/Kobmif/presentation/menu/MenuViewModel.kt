package com.Faprire4.Kobmif.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.domain.model.CourtSettings
import com.Faprire4.Kobmif.domain.repository.CaseProgressRepository
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: CaseProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            val record = progressRepository.record()
            val options = settingsRepository.settings()
            _state.update {
                it.copy(
                    casesSolved = record.casesSolved,
                    chaptersOpen = record.unlockedChapters,
                    nextChapterIndex = (record.unlockedChapters - 1).coerceAtLeast(0),
                    bestEnding = record.bestEnding,
                    settings = options
                )
            }
        }
    }

    fun openSettings() = _state.update { it.copy(settingsOpen = true) }

    fun closeSettings() = _state.update { it.copy(settingsOpen = false, resetOpen = false) }

    fun toggleConfirmAccusation() = applySettings { it.copy(confirmAccusation = !it.confirmAccusation) }

    fun toggleCandleMotion() = applySettings { it.copy(candleMotion = !it.candleMotion) }

    fun toggleMaskColours() = applySettings { it.copy(maskColours = !it.maskColours) }

    fun askReset() = _state.update { it.copy(resetOpen = true) }

    fun dismissReset() = _state.update { it.copy(resetOpen = false) }

    fun confirmReset() {
        viewModelScope.launch {
            progressRepository.clearRecords()
            _state.update { it.copy(resetOpen = false, settingsOpen = false) }
            reload()
        }
    }

    private fun applySettings(change: (CourtSettings) -> CourtSettings) {
        viewModelScope.launch {
            val updated = change(_state.value.settings)
            settingsRepository.store(updated)
            _state.update { it.copy(settings = updated) }
        }
    }
}
