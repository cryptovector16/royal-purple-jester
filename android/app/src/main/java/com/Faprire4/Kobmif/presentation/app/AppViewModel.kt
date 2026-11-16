package com.Faprire4.Kobmif.presentation.app

import androidx.lifecycle.ViewModel
import com.Faprire4.Kobmif.core.navigation.Screen
import com.Faprire4.Kobmif.domain.model.CaseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val backStack = ArrayDeque<Screen>()

    fun finishSplash() {
        backStack.clear()
        _screen.value = Screen.Menu
        _canGoBack.value = false
    }

    fun openCaseSelect() = push(Screen.CaseSelect)

    fun openSettings() = push(Screen.Settings)

    fun openGame(chapterIndex: Int, attempt: Int) = push(Screen.Game(chapterIndex, attempt))

    fun restartGame(chapterIndex: Int, attempt: Int) {
        backStack.clear()
        backStack.addLast(Screen.Menu)
        _screen.value = Screen.Game(chapterIndex, attempt)
        _canGoBack.value = true
    }

    fun openResult(result: CaseResult) {
        backStack.clear()
        backStack.addLast(Screen.Menu)
        _screen.value = Screen.GameOver(result)
        _canGoBack.value = true
    }

    fun goMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        _canGoBack.value = false
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        _canGoBack.value = backStack.isNotEmpty()
    }

    private fun push(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) {
            backStack.addLast(current)
        }
        _screen.value = target
        _canGoBack.value = backStack.isNotEmpty()
    }
}
