package com.Faprire4.Kobmif.presentation.settings

import com.Faprire4.Kobmif.core.config.GameConfig

data class SettingsUiState(
    val questionsPerCase: Int = GameConfig.QUESTIONS_COURT,
    val sealHints: Boolean = true,
    val steadyCandlelight: Boolean = false,
    val autoFilterBoard: Boolean = false
)
