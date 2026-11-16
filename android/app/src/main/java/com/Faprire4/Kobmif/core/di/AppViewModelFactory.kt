package com.Faprire4.Kobmif.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Faprire4.Kobmif.domain.repository.ProgressRepository
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import com.Faprire4.Kobmif.domain.usecase.CrossExamineUseCase
import com.Faprire4.Kobmif.domain.usecase.GenerateCaseUseCase
import com.Faprire4.Kobmif.domain.usecase.ResolveVerdictUseCase
import com.Faprire4.Kobmif.presentation.app.AppViewModel
import com.Faprire4.Kobmif.presentation.caseselect.CaseSelectViewModel
import com.Faprire4.Kobmif.presentation.game.GameViewModel
import com.Faprire4.Kobmif.presentation.gameover.GameOverViewModel
import com.Faprire4.Kobmif.presentation.menu.MenuViewModel
import com.Faprire4.Kobmif.presentation.settings.SettingsViewModel
import com.Faprire4.Kobmif.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generateCase: GenerateCaseUseCase,
    private val crossExamine: CrossExamineUseCase,
    private val resolveVerdict: ResolveVerdictUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(progressRepository)

            modelClass.isAssignableFrom(CaseSelectViewModel::class.java) ->
                CaseSelectViewModel(progressRepository)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    progressRepository = progressRepository,
                    settingsRepository = settingsRepository,
                    generateCase = generateCase,
                    crossExamine = crossExamine,
                    resolveVerdict = resolveVerdict
                )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(progressRepository)

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settingsRepository)

            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
