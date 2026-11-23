package com.Faprire4.Kobmif.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.Case
import com.Faprire4.Kobmif.domain.model.Claim
import com.Faprire4.Kobmif.domain.model.ClaimPredicate
import com.Faprire4.Kobmif.domain.model.SealState
import com.Faprire4.Kobmif.domain.model.Verdict
import com.Faprire4.Kobmif.domain.repository.CaseProgressRepository
import com.Faprire4.Kobmif.domain.repository.CourtSessionRepository
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import com.Faprire4.Kobmif.domain.usecase.DetectContradictionUseCase
import com.Faprire4.Kobmif.domain.usecase.GenerateCaseUseCase
import com.Faprire4.Kobmif.domain.usecase.ResolveEndingUseCase
import com.Faprire4.Kobmif.domain.usecase.ScoreVerdictUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val progressRepository: CaseProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val sessionRepository: CourtSessionRepository,
    private val generateCase: GenerateCaseUseCase,
    private val detectContradiction: DetectContradictionUseCase,
    private val scoreVerdict: ScoreVerdictUseCase,
    private val resolveEnding: ResolveEndingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<Unit>(Channel.BUFFERED)
    val verdictReached: Flow<Unit> = eventChannel.receiveAsFlow()

    private var activeCase: Case? = null
    private var voteJob: Job? = null
    private var sealJob: Job? = null
    private var loadedSession = -1

    fun openChapter(chapterIndex: Int, session: Int) {
        if (session == loadedSession && activeCase != null) return
        loadedSession = session
        voteJob?.cancel()
        sealJob?.cancel()
        viewModelScope.launch {
            val chapter = progressRepository.chapter(chapterIndex)
            val attempt = progressRepository.nextAttempt(chapterIndex)
            val generated = generateCase.invoke(chapter, attempt)
            val options = settingsRepository.settings()
            activeCase = generated
            _state.value = GameUiState(
                ready = true,
                chapterIndex = chapterIndex,
                seed = generated.seed,
                maskedCount = generated.maskedCount,
                questionsLeft = generated.questions,
                questionsTotal = generated.questions,
                patience = generated.patience,
                patienceTotal = generated.patience,
                insight = 0,
                claims = generated.claims.map { claim ->
                    ClaimRow(
                        id = claim.id,
                        speaker = claim.speaker,
                        predicate = claim.predicate,
                        core = claim.core,
                        revealed = false,
                        seal = SealState.UNMARKED,
                        locked = false
                    )
                },
                nodes = generated.courtiers.map { courtier ->
                    NodeState(
                        index = courtier.index,
                        revealedRole = null,
                        claimedMotive = null,
                        claimsSpoken = 0
                    )
                },
                graphRotationDeg = generated.graphRotationDeg,
                maskColours = options.maskColours,
                confirmAccusation = options.confirmAccusation
            )
        }
    }

    fun selectTab(index: Int) = _state.update { it.copy(tab = index) }

    fun selectCourtier(index: Int) = _state.update {
        it.copy(selected = if (it.selected == index) -1 else index)
    }

    fun dealClaim() {
        val current = _state.value
        if (!current.canDeal) return
        val next = current.claims.firstOrNull { !it.revealed && it.speaker == current.selected }
            ?: current.claims.first { !it.revealed }
        val predicate = next.predicate
        val newEdges = current.edges.toMutableList()
        if (predicate is ClaimPredicate.Kin) {
            newEdges.removeAll { sameEdge(it, predicate.first, predicate.second) }
            newEdges.add(GraphEdge(predicate.first, predicate.second, kin = true))
        }
        if (predicate is ClaimPredicate.Rival) {
            newEdges.removeAll { sameEdge(it, predicate.first, predicate.second) }
            newEdges.add(GraphEdge(predicate.first, predicate.second, kin = false))
        }
        val nodes = current.nodes.map { node ->
            var updated = node
            if (node.index == next.speaker) {
                updated = updated.copy(claimsSpoken = node.claimsSpoken + 1)
            }
            if (predicate is ClaimPredicate.HasMotive && node.index == predicate.target) {
                updated = updated.copy(claimedMotive = predicate.motive)
            }
            updated
        }
        _state.update {
            it.copy(
                claims = it.claims.map { row -> if (row.id == next.id) row.copy(revealed = true) else row },
                nodes = nodes,
                edges = newEdges,
                questionsLeft = it.questionsLeft - 1,
                selected = if (it.selected >= 0) it.selected else next.speaker,
                lockFeedback = 0
            )
        }
        val after = _state.value
        if (after.questionsLeft <= 0 || after.claims.none { !it.revealed }) {
            callTheVote()
        }
    }

    fun cycleSeal(claimId: Int) {
        val current = _state.value
        if (current.blocked) return
        val target = current.claims.firstOrNull { it.id == claimId && it.revealed } ?: return
        val next = when (target.seal) {
            SealState.UNMARKED -> SealState.MARKED_TRUE
            SealState.MARKED_TRUE -> SealState.MARKED_FALSE
            SealState.MARKED_FALSE -> SealState.UNMARKED
        }
        _state.update {
            it.copy(
                claims = it.claims.map { row -> if (row.id == claimId) row.copy(seal = next) else row },
                lockFeedback = 0
            )
        }
    }

    fun lockPair() {
        val current = _state.value
        if (!current.canLock) return
        val case = activeCase ?: return
        val first = current.markedClaims[0]
        val second = current.markedClaims[1]
        val genuine = detectContradiction.invoke(
            Claim(first.id, first.speaker, first.predicate),
            first.seal == SealState.MARKED_TRUE,
            Claim(second.id, second.speaker, second.predicate),
            second.seal == SealState.MARKED_TRUE,
            case.maskedCount
        )
        if (genuine) {
            _state.update {
                it.copy(
                    insight = it.insight + GameConfig.INSIGHT_PER_LOCK,
                    locks = it.locks + 1,
                    lockFeedback = 1,
                    claims = it.claims.map { row ->
                        if (row.id == first.id || row.id == second.id) {
                            row.copy(locked = true, seal = SealState.UNMARKED)
                        } else {
                            row
                        }
                    }
                )
            }
        } else {
            _state.update {
                it.copy(
                    patience = (it.patience - GameConfig.WRONG_LOCK_PATIENCE_COST).coerceAtLeast(0),
                    wrongLocks = it.wrongLocks + 1,
                    lockFeedback = -1,
                    claims = it.claims.map { row ->
                        if (row.id == first.id || row.id == second.id) {
                            row.copy(seal = SealState.UNMARKED)
                        } else {
                            row
                        }
                    }
                )
            }
            if (_state.value.patience <= 0) forceVote()
        }
    }

    fun sealOfTruth() {
        val current = _state.value
        if (!current.canUnmask) return
        val case = activeCase ?: return
        val index = current.selected
        val role = case.courtiers[index].role
        _state.update {
            it.copy(
                insight = it.insight - GameConfig.UNMASK_COST,
                unmaskedCount = it.unmaskedCount + 1,
                nodes = it.nodes.map { node ->
                    if (node.index == index) node.copy(revealedRole = role) else node
                }
            )
        }
    }

    fun openAccusation() {
        if (_state.value.blocked) return
        voteJob?.cancel()
        _state.update { it.copy(accusing = true) }
    }

    fun dismissAccusation() {
        _state.update { it.copy(accusing = false) }
        if (_state.value.voteCalled) callTheVote()
    }

    fun nameHeir(index: Int) {
        if (_state.value.blocked) return
        resolve(index, GameConfig.VERDICT_REVEAL_MS)
    }

    fun pause() {
        if (_state.value.blocked) return
        voteJob?.cancel()
        _state.update { it.copy(paused = true) }
    }

    fun resume() {
        _state.update { it.copy(paused = false) }
        if (_state.value.voteCalled) callTheVote()
    }

    fun onScreenLeft() {
        voteJob?.cancel()
        sealJob?.cancel()
    }

    private fun sameEdge(edge: GraphEdge, first: Int, second: Int): Boolean =
        (edge.first == first && edge.second == second) || (edge.first == second && edge.second == first)

    private fun callTheVote() {
        if (_state.value.sealing) return
        _state.update { it.copy(voteCalled = true) }
        voteJob?.cancel()
        voteJob = viewModelScope.launch {
            delay(GameConfig.COUNCIL_VOTE_GRACE_MS)
            resolve(-1, GameConfig.FORCED_VOTE_DELAY_MS)
        }
    }

    private fun forceVote() {
        voteJob?.cancel()
        _state.update { it.copy(voteCalled = true) }
        resolve(-1, GameConfig.FORCED_VOTE_DELAY_MS)
    }

    private fun resolve(accusedIndex: Int, waitMs: Long) {
        val case = activeCase ?: return
        if (_state.value.sealing) return
        voteJob?.cancel()
        _state.update { it.copy(sealing = true, accusing = false, paused = false) }
        sealJob?.cancel()
        sealJob = viewModelScope.launch {
            delay(waitMs)
            val snapshot = _state.value
            val confidence = scoreVerdict.invoke(
                snapshot.locks,
                snapshot.questionsLeft,
                snapshot.unmaskedCount,
                snapshot.wrongLocks
            )
            val ending = resolveEnding.invoke(
                accusedIndex >= 0,
                accusedIndex == case.heirIndex,
                confidence
            )
            val verdict = Verdict(
                chapterIndex = case.chapterIndex,
                seed = case.seed,
                ending = ending,
                heirIndex = case.heirIndex,
                accusedIndex = accusedIndex,
                confidence = confidence,
                locks = snapshot.locks,
                wrongLocks = snapshot.wrongLocks,
                questionsLeft = snapshot.questionsLeft,
                unmasked = snapshot.unmaskedCount
            )
            progressRepository.storeVerdict(verdict)
            sessionRepository.publish(verdict)
            eventChannel.send(Unit)
        }
    }
}
