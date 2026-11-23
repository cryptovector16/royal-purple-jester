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
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.navigation.Screen
import com.Faprire4.Kobmif.core.theme.InkSilhouette
import com.Faprire4.Kobmif.presentation.chapters.ChapterSelectScreen
import com.Faprire4.Kobmif.presentation.game.GameScreen
import com.Faprire4.Kobmif.presentation.gameover.GameOverScreen
import com.Faprire4.Kobmif.presentation.menu.MenuScreen
import com.Faprire4.Kobmif.presentation.rules.RulesScreen
import com.Faprire4.Kobmif.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val model: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by model.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen !is Screen.Splash && screen !is Screen.Menu) {
        model.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InkSilhouette)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(animationSpec = tween(320)) togetherWith fadeOut(animationSpec = tween(240))
            },
            label = "courtScreens"
        ) { target ->
            when (target) {
                is Screen.Splash -> SplashScreen(onFinished = { model.toMenu() })

                is Screen.Menu -> MenuScreen(
                    onPlay = { chapterIndex -> model.startCase(chapterIndex) },
                    onChapters = { model.open(Screen.Chapters) },
                    onRules = { model.open(Screen.Rules) }
                )

                is Screen.Chapters -> ChapterSelectScreen(
                    onOpenChapter = { chapterIndex -> model.startCase(chapterIndex) },
                    onMenu = { model.toMenu() }
                )

                is Screen.Rules -> RulesScreen(onMenu = { model.toMenu() })

                is Screen.Game -> GameScreen(
                    chapterIndex = target.chapterIndex,
                    session = target.session,
                    onVerdict = { model.showVerdict() },
                    onMenu = { model.toMenu() },
                    onRules = { model.open(Screen.Rules) }
                )

                is Screen.GameOver -> GameOverScreen(
                    onPlayAgain = { chapterIndex -> model.startCase(chapterIndex) },
                    onNextChapter = { chapterIndex -> model.startCase(chapterIndex) },
                    onMenu = { model.toMenu() }
                )
            }
        }
    }
}
