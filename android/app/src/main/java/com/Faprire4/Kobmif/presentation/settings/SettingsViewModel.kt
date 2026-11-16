package com.Faprire4.Kobmif.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.domain.model.GameSettings
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val stored = settingsRepository.read()
            _state.update {
                it.copy(
                    questionsPerCase = stored.questionsPerCase,
                    sealHints = stored.sealHints,
                    steadyCandlelight = stored.steadyCandlelight,
                    autoFilterBoard = stored.autoFilterBoard
                )
            }
        }
    }

    fun onInquiryLength(value: Int) {
        _state.update { it.copy(questionsPerCase = value) }
        persist()
    }

    fun onSealHints(value: Boolean) {
        _state.update { it.copy(sealHints = value) }
        persist()
    }

    fun onSteadyCandlelight(value: Boolean) {
        _state.update { it.copy(steadyCandlelight = value) }
        persist()
    }

    fun onAutoFilterBoard(value: Boolean) {
        _state.update { it.copy(autoFilterBoard = value) }
        persist()
    }

    private fun persist() {
        viewModelScope.launch {
            val current = _state.value
            settingsRepository.write(
                GameSettings(
                    questionsPerCase = current.questionsPerCase,
                    sealHints = current.sealHints,
                    steadyCandlelight = current.steadyCandlelight,
                    autoFilterBoard = current.autoFilterBoard
                )
            )
        }
    }
}
