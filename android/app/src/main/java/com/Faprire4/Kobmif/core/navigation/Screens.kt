package com.Faprire4.Kobmif.core.navigation

import com.Faprire4.Kobmif.domain.model.CaseResult

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object CaseSelect : Screen

    data object Settings : Screen

    data class Game(val chapterIndex: Int, val attempt: Int) : Screen

    data class GameOver(val result: CaseResult) : Screen
}
