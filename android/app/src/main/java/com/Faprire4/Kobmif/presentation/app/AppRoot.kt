package com.Faprire4.Kobmif.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.navigation.Screen
import com.Faprire4.Kobmif.core.theme.InkBlack
import com.Faprire4.Kobmif.presentation.caseselect.CaseSelectScreen
import com.Faprire4.Kobmif.presentation.game.GameScreen
import com.Faprire4.Kobmif.presentation.gameover.GameOverScreen
import com.Faprire4.Kobmif.presentation.menu.MenuScreen
import com.Faprire4.Kobmif.presentation.settings.SettingsScreen
import com.Faprire4.Kobmif.presentation.splash.SplashScreen

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()
    var attempt by remember { mutableIntStateOf(0) }

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    Box(modifier = modifier.fillMaxSize().background(InkBlack)) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = { fadeIn(tween(420)) togetherWith fadeOut(tween(300)) },
            label = "screenTransition"
        ) { target ->
            when (target) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.finishSplash() }
                )

                is Screen.Menu -> MenuScreen(
                    onPlay = { chapterIndex ->
                        attempt += 1
                        appViewModel.openGame(chapterIndex, attempt)
                    },
                    onCaseSelect = { appViewModel.openCaseSelect() },
                    onSettings = { appViewModel.openSettings() }
                )

                is Screen.CaseSelect -> CaseSelectScreen(
                    onOpenChapter = { chapterIndex ->
                        attempt += 1
                        appViewModel.openGame(chapterIndex, attempt)
                    },
                    onMenu = { appViewModel.goMenu() }
                )

                is Screen.Settings -> SettingsScreen(
                    onMenu = { appViewModel.goMenu() }
                )

                is Screen.Game -> GameScreen(
                    chapterIndex = target.chapterIndex,
                    attempt = target.attempt,
                    onGameOver = { result -> appViewModel.openResult(result) },
                    onMenu = { appViewModel.goMenu() }
                )

                is Screen.GameOver -> GameOverScreen(
                    result = target.result,
                    onPlayAgain = {
                        attempt += 1
                        appViewModel.restartGame(target.result.chapterIndex, attempt)
                    },
                    onNextCase = { chapterIndex ->
                        attempt += 1
                        appViewModel.restartGame(chapterIndex, attempt)
                    },
                    onMenu = { appViewModel.goMenu() }
                )
            }
        }
    }
}
