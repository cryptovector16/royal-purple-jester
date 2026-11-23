package com.Faprire4.Kobmif.presentation.game

import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.ClaimPredicate
import com.Faprire4.Kobmif.domain.model.CourtierRole
import com.Faprire4.Kobmif.domain.model.Motive
import com.Faprire4.Kobmif.domain.model.SealState

data class ClaimRow(
    val id: Int,
    val speaker: Int,
    val predicate: ClaimPredicate,
    val core: Boolean,
    val revealed: Boolean,
    val seal: SealState,
    val locked: Boolean
)

data class NodeState(
    val index: Int,
    val revealedRole: CourtierRole?,
    val claimedMotive: Motive?,
    val claimsSpoken: Int
)

data class GraphEdge(
    val first: Int,
    val second: Int,
    val kin: Boolean
)

data class GameUiState(
    val ready: Boolean = false,
    val chapterIndex: Int = 0,
    val seed: Int = 0,
    val maskedCount: Int = 0,
    val questionsLeft: Int = 0,
    val questionsTotal: Int = 0,
    val patience: Int = 0,
    val patienceTotal: Int = GameConfig.START_PATIENCE,
    val insight: Int = 0,
    val locks: Int = 0,
    val wrongLocks: Int = 0,
    val unmaskedCount: Int = 0,
    val claims: List<ClaimRow> = emptyList(),
    val nodes: List<NodeState> = emptyList(),
    val edges: List<GraphEdge> = emptyList(),
    val graphRotationDeg: Float = 0f,
    val selected: Int = -1,
    val tab: Int = 0,
    val voteCalled: Boolean = false,
    val sealing: Boolean = false,
    val paused: Boolean = false,
    val accusing: Boolean = false,
    val lockFeedback: Int = 0,
    val maskColours: Boolean = true,
    val confirmAccusation: Boolean = true
) {
    val revealedClaims: List<ClaimRow>
        get() = claims.filter { it.revealed }

    val markedClaims: List<ClaimRow>
        get() = claims.filter { it.revealed && it.seal != SealState.UNMARKED }

    val blocked: Boolean
        get() = !ready || sealing

    val canDeal: Boolean
        get() = !blocked && questionsLeft > 0 && claims.any { !it.revealed }

    val canLock: Boolean
        get() = !blocked && markedClaims.size == 2 && markedClaims.all { it.core }

    val canUnmask: Boolean
        get() = !blocked && insight >= GameConfig.UNMASK_COST && selected >= 0 &&
            nodes.getOrNull(selected)?.revealedRole == null

    val canAccuse: Boolean
        get() = !blocked
}
