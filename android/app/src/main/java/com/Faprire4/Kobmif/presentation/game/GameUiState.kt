package com.Faprire4.Kobmif.presentation.game

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.Claimant
import com.Faprire4.Kobmif.domain.model.Mark
import com.Faprire4.Kobmif.domain.model.Statement
import com.Faprire4.Kobmif.domain.model.Tie

enum class BoardPane {
    EVIDENCE,
    TIES
}

sealed interface GameDialog {

    data object None : GameDialog

    data object Pause : GameDialog

    data class Vote(val selectedId: Int?) : GameDialog

    data class Contradiction(val firstId: Int, val secondId: Int) : GameDialog
}

data class GameUiState(
    val loading: Boolean = true,
    val chapterIndex: Int = 0,
    val chapterTitleRes: Int = 0,
    val claimants: List<Claimant> = emptyList(),
    val revealed: List<Statement> = emptyList(),
    val boardNotes: List<Statement> = emptyList(),
    val marks: Map<Int, Mark> = emptyMap(),
    val unmasked: Set<Int> = emptySet(),
    val selectedClaimantId: Int? = null,
    val selectedNoteIds: List<Int> = emptyList(),
    val stampedNoteIds: Set<Int> = emptySet(),
    val hintedNoteIds: Set<Int> = emptySet(),
    val contradictionsBy: Map<Int, Int> = emptyMap(),
    val contradictions: Int = 0,
    val questionsLeft: Int = GameConfig.QUESTIONS_COURT,
    val questionBudget: Int = GameConfig.QUESTIONS_COURT,
    val favour: Int = GameConfig.FAVOUR_START,
    val favourMax: Int = GameConfig.FAVOUR_MAX,
    val ties: List<Tie> = emptyList(),
    val pane: BoardPane = BoardPane.EVIDENCE,
    val dialog: GameDialog = GameDialog.None,
    val missVisible: Boolean = false,
    val steady: Boolean = false,
    val autoFilter: Boolean = false,
    val sealHints: Boolean = true,
    val canQuestion: Boolean = false,
    val canUnmask: Boolean = false,
    val canCrossExamine: Boolean = false
)
