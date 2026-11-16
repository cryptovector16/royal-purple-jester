package com.Faprire4.Kobmif.data.repository

import com.Faprire4.Kobmif.data.local.JesterPreferences
import com.Faprire4.Kobmif.domain.model.GameSettings
import com.Faprire4.Kobmif.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val preferences: JesterPreferences
) : SettingsRepository {

    override fun read(): GameSettings = GameSettings(
        questionsPerCase = preferences.questionsPerCase,
        sealHints = preferences.sealHints,
        steadyCandlelight = preferences.steadyCandlelight,
        autoFilterBoard = preferences.autoFilterBoard
    )

    override fun write(settings: GameSettings) {
        preferences.questionsPerCase = settings.questionsPerCase
        preferences.sealHints = settings.sealHints
        preferences.steadyCandlelight = settings.steadyCandlelight
        preferences.autoFilterBoard = settings.autoFilterBoard
    }
}
