package com.Faprire4.Kobmif.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _finished = MutableStateFlow(false)
    val finished: StateFlow<Boolean> = _finished.asStateFlow()

    private val _motion = MutableStateFlow(true)
    val motion: StateFlow<Boolean> = _motion.asStateFlow()

    init {
        _motion.value = settingsRepository.settings().candleMotion
        viewModelScope.launch {
            delay(GameConfig.LOADER_DURATION_MS)
            _finished.value = true
        }
    }
}
