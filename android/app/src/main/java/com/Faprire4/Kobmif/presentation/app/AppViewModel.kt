package com.Faprire4.Kobmif.presentation.app

import androidx.lifecycle.ViewModel
import com.Faprire4.Kobmif.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val backStack = ArrayDeque<Screen>()
    private var session = 0

    fun open(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) backStack.addLast(current)
        _screen.value = target
    }

    fun toMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun startCase(chapterIndex: Int) {
        session += 1
        backStack.clear()
        backStack.addLast(Screen.Menu)
        _screen.value = Screen.Game(chapterIndex, session)
    }

    fun showVerdict() {
        backStack.clear()
        backStack.addLast(Screen.Menu)
        _screen.value = Screen.GameOver
    }

    fun back() {
        val previous = backStack.removeLastOrNull()
        _screen.value = previous ?: Screen.Menu
    }
}
