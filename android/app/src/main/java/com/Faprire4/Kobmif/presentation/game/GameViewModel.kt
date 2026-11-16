package com.Faprire4.Kobmif.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.domain.model.CaseResult
import com.Faprire4.Kobmif.domain.model.CaseSetup
import com.Faprire4.Kobmif.domain.model.Mark
import com.Faprire4.Kobmif.domain.model.Statement
import com.Faprire4.Kobmif.domain.repository.ProgressRepository
import com.Faprire4.Kobmif.domain.repository.SettingsRepository
import com.Faprire4.Kobmif.domain.usecase.CrossExamineUseCase
import com.Faprire4.Kobmif.domain.usecase.GenerateCaseUseCase
import com.Faprire4.Kobmif.domain.usecase.ResolveVerdictUseCase
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

sealed interface GameEvent {
    data class Finished(val result: CaseResult) : GameEvent
}

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generateCase: GenerateCaseUseCase,
    private val crossExamine: CrossExamineUseCase,
    private val resolveVerdict: ResolveVerdictUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var setup: CaseSetup? = null
    private var revealCursor = HashMap<Int, Int>()
    private var holdJob: Job? = null
    private var misses = 0
    private var finished = false
    private var startedKey: String? = null

    fun startCase(chapterIndex: Int, attempt: Int) {
        val key = "$chapterIndex:$attempt"
        if (startedKey == key) {
            return
        }
        startedKey = key
        finished = false
        misses = 0
        holdJob?.cancel()
        viewModelScope.launch {
            val settings = settingsRepository.read()
            val generated = generateCase(chapterIndex, attempt, settings.questionsPerCase)
            setup = generated
            revealCursor = HashMap()
            generated.claimants.forEach { revealCursor[it.id] = 0 }
            _state.value = GameUiState(
                loading = false,
                chapterIndex = generated.chapterIndex,
                chapterTitleRes = generated.chapterTitleRes,
                claimants = generated.claimants,
                marks = generated.claimants.associate { it.id to Mark.NONE },
                questionsLeft = generated.questionBudget,
                questionBudget = generated.questionBudget,
                ties = generated.ties,
                steady = settings.steadyCandlelight,
                autoFilter = settings.autoFilterBoard,
                sealHints = settings.sealHints
            )
            recompute()
        }
    }

    fun onSelectClaimant(id: Int) {
        val current = _state.value
        if (current.loading || finished) {
            return
        }
        if (current.selectedClaimantId != id) {
            _state.update { it.copy(selectedClaimantId = id) }
        } else {
            val next = when (current.marks[id] ?: Mark.NONE) {
                Mark.NONE -> Mark.SUSPECT
                Mark.SUSPECT -> Mark.CLEARED
                Mark.CLEARED -> Mark.NONE
            }
            _state.update { it.copy(marks = it.marks + (id to next)) }
        }
        recompute()
    }

    fun onQuestion() {
        val current = _state.value
        val active = setup ?: return
        val speaker = current.selectedClaimantId ?: return
        if (!current.canQuestion) {
            return
        }
        val queue = active.queues[speaker].orEmpty()
        val cursor = revealCursor[speaker] ?: 0
        if (cursor >= queue.size) {
            return
        }
        revealCursor[speaker] = cursor + 1
        _state.update {
            it.copy(
                revealed = it.revealed + queue[cursor],
                questionsLeft = (it.questionsLeft - 1).coerceAtLeast(0)
            )
        }
        recompute()
        if (_state.value.questionsLeft == 0) {
            callCouncil()
        }
    }

    fun onToggleNote(noteId: Int) {
        val current = _state.value
        if (current.loading || finished || current.stampedNoteIds.contains(noteId)) {
            return
        }
        val selection = current.selectedNoteIds.toMutableList()
        if (selection.contains(noteId)) {
            selection.remove(noteId)
        } else {
            if (selection.size >= 2) {
                selection.removeAt(0)
            }
            selection.add(noteId)
        }
        _state.update { it.copy(selectedNoteIds = selection.toList(), missVisible = false) }
        recompute()
    }

    fun onCrossExamine() {
        val current = _state.value
        if (!current.canCrossExamine) {
            return
        }
        val first = current.revealed.firstOrNull { it.id == current.selectedNoteIds[0] } ?: return
        val second = current.revealed.firstOrNull { it.id == current.selectedNoteIds[1] } ?: return
        if (crossExamine(first, second)) {
            val liar = if (!first.truthful) first.speakerId else second.speakerId
            _state.update {
                it.copy(
                    stampedNoteIds = it.stampedNoteIds + first.id + second.id,
                    selectedNoteIds = emptyList(),
                    contradictions = it.contradictions + 1,
                    contradictionsBy = it.contradictionsBy + (liar to ((it.contradictionsBy[liar] ?: 0) + 1)),
                    favour = (it.favour + 1).coerceAtMost(GameConfig.FAVOUR_MAX),
                    dialog = GameDialog.Contradiction(first.id, second.id),
                    missVisible = false
                )
            }
            recompute()
            holdJob?.cancel()
            holdJob = viewModelScope.launch {
                delay(GameConfig.CONTRADICTION_HOLD_MS)
                dismissHold()
            }
        } else {
            misses++
            _state.update {
                it.copy(
                    selectedNoteIds = emptyList(),
                    favour = (it.favour - 1).coerceAtLeast(0),
                    missVisible = true
                )
            }
            recompute()
            holdJob?.cancel()
            holdJob = viewModelScope.launch {
                delay(GameConfig.MISS_HOLD_MS)
                _state.update { it.copy(missVisible = false) }
            }
            if (_state.value.favour == 0) {
                callCouncil()
            }
        }
    }

    fun onDismissContradiction() {
        holdJob?.cancel()
        dismissHold()
    }

    fun onUnmask() {
        val current = _state.value
        val claimant = current.selectedClaimantId ?: return
        if (!current.canUnmask) {
            return
        }
        _state.update {
            it.copy(
                unmasked = it.unmasked + claimant,
                questionsLeft = (it.questionsLeft - GameConfig.UNMASK_COST).coerceAtLeast(0)
            )
        }
        recompute()
        if (_state.value.questionsLeft == 0) {
            callCouncil()
        }
    }

    fun onSwitchPane(pane: BoardPane) {
        _state.update { it.copy(pane = pane) }
    }

    fun onOpenVote() {
        if (finished) {
            return
        }
        val current = _state.value
        val preselected = current.marks.entries
            .firstOrNull { it.value == Mark.CLEARED }
            ?.key
            ?: current.selectedClaimantId
        _state.update { it.copy(dialog = GameDialog.Vote(preselected)) }
    }

    fun onVoteSelect(id: Int) {
        val dialog = _state.value.dialog
        if (dialog is GameDialog.Vote) {
            _state.update { it.copy(dialog = GameDialog.Vote(id)) }
        }
    }

    fun onConfirmVote() {
        val dialog = _state.value.dialog
        if (dialog !is GameDialog.Vote) {
            return
        }
        val accused = dialog.selectedId ?: return
        finish(accused)
    }

    fun onCancelVote() {
        _state.update { it.copy(dialog = GameDialog.None) }
    }

    fun onPause() {
        if (finished) {
            return
        }
        _state.update { it.copy(dialog = GameDialog.Pause) }
    }

    fun onResume() {
        _state.update { it.copy(dialog = GameDialog.None) }
    }

    fun pause() {
        holdJob?.cancel()
        holdJob = null
    }

    override fun onCleared() {
        super.onCleared()
        pause()
    }

    private fun dismissHold() {
        _state.update {
            if (it.dialog is GameDialog.Contradiction) it.copy(dialog = GameDialog.None) else it
        }
    }

    private fun callCouncil() {
        if (finished) {
            return
        }
        val current = _state.value
        val accused = current.marks.entries.firstOrNull { it.value == Mark.CLEARED }?.key
            ?: current.marks.entries.firstOrNull { it.value == Mark.NONE }?.key
            ?: current.claimants.firstOrNull()?.id
            ?: 0
        finish(accused)
    }

    private fun finish(accusedId: Int) {
        val active = setup ?: return
        if (finished) {
            return
        }
        finished = true
        holdJob?.cancel()
        val current = _state.value
        val result = resolveVerdict(
            setup = active,
            accusedId = accusedId,
            contradictions = current.contradictions,
            motivesRevealed = current.unmasked.size,
            questionsLeft = current.questionsLeft,
            favour = current.favour
        )
        viewModelScope.launch {
            progressRepository.record(result)
            _events.send(GameEvent.Finished(result))
        }
    }

    private fun recompute() {
        val active = setup
        _state.update { current ->
            val speaker = current.selectedClaimantId
            val queueLeft = if (active == null || speaker == null) {
                0
            } else {
                active.queues[speaker].orEmpty().size - (revealCursor[speaker] ?: 0)
            }
            val board = boardFor(current)
            current.copy(
                boardNotes = board,
                hintedNoteIds = hintsFor(current),
                canQuestion = !current.loading &&
                    !finished &&
                    speaker != null &&
                    current.questionsLeft > 0 &&
                    queueLeft > 0,
                canUnmask = !current.loading &&
                    !finished &&
                    speaker != null &&
                    !current.unmasked.contains(speaker) &&
                    (current.contradictionsBy[speaker] ?: 0) >= GameConfig.UNMASK_CONTRADICTIONS &&
                    current.questionsLeft >= GameConfig.UNMASK_COST,
                canCrossExamine = !current.loading &&
                    !finished &&
                    current.selectedNoteIds.size == 2
            )
        }
    }

    private fun boardFor(current: GameUiState): List<Statement> {
        val speaker = current.selectedClaimantId
        val mark = if (speaker == null) Mark.NONE else current.marks[speaker] ?: Mark.NONE
        return if (current.autoFilter && speaker != null && mark != Mark.NONE) {
            current.revealed.filter { it.speakerId == speaker }
        } else {
            current.revealed
        }
    }

    private fun hintsFor(current: GameUiState): Set<Int> {
        val settingsOn = current.sealHints && misses >= GameConfig.SEAL_HINT_AFTER_MISSES
        if (!settingsOn) {
            return emptySet()
        }
        val open = current.revealed.filterNot { current.stampedNoteIds.contains(it.id) }
        val hinted = mutableSetOf<Int>()
        for (i in open.indices) {
            for (j in i + 1 until open.size) {
                if (crossExamine(open[i], open[j])) {
                    hinted.add(open[i].id)
                    hinted.add(open[j].id)
                }
            }
        }
        return hinted
    }
}
