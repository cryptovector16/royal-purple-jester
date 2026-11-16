package com.Faprire4.Kobmif.domain.repository

import com.Faprire4.Kobmif.domain.model.GameSettings

interface SettingsRepository {
    fun read(): GameSettings
    fun write(settings: GameSettings)
}
