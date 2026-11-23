package com.Faprire4.Kobmif.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.Faprire4.Kobmif.presentation.app.AppViewModel
import com.Faprire4.Kobmif.presentation.chapters.ChaptersViewModel
import com.Faprire4.Kobmif.presentation.game.GameViewModel
import com.Faprire4.Kobmif.presentation.gameover.GameOverViewModel
import com.Faprire4.Kobmif.presentation.menu.MenuViewModel
import com.Faprire4.Kobmif.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val container: ServiceLocator.Container
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel() as T

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel(container.settingsRepository) as T

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(container.progressRepository, container.settingsRepository) as T

            modelClass.isAssignableFrom(ChaptersViewModel::class.java) ->
                ChaptersViewModel(container.progressRepository) as T

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    container.progressRepository,
                    container.settingsRepository,
                    container.sessionRepository,
                    container.generateCase,
                    container.detectContradiction,
                    container.scoreVerdict,
                    container.resolveEnding
                ) as T

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(container.sessionRepository, container.progressRepository) as T

            else -> throw IllegalArgumentException(modelClass.name)
        }
    }
}
