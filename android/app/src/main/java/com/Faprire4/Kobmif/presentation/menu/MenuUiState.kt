package com.Faprire4.Kobmif.presentation.menu

import com.Faprire4.Kobmif.domain.model.Ending
import com.Faprire4.Kobmif.domain.model.CourtSettings

data class MenuUiState(
    val casesSolved: Int = 0,
    val chaptersOpen: Int = 1,
    val nextChapterIndex: Int = 0,
    val bestEnding: Ending? = null,
    val settings: CourtSettings = CourtSettings(
        confirmAccusation = true,
        candleMotion = true,
        maskColours = true
    ),
    val settingsOpen: Boolean = false,
    val resetOpen: Boolean = false
) {
    val hasRecord: Boolean
        get() = casesSolved > 0 || bestEnding != null
}
