package com.Faprire4.Kobmif.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Chapters : Screen
    data object Rules : Screen
    data class Game(val chapterIndex: Int, val session: Int) : Screen
    data object GameOver : Screen
}
