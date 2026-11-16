package com.Faprire4.Kobmif.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.GoldBorder
import com.Faprire4.Kobmif.core.theme.HeaderScrim
import com.Faprire4.Kobmif.core.theme.InertBorder
import com.Faprire4.Kobmif.core.theme.InkBlack
import com.Faprire4.Kobmif.core.theme.ScrimMid
import com.Faprire4.Kobmif.core.theme.ScrimSoft
import com.Faprire4.Kobmif.core.theme.SealCrimson
import com.Faprire4.Kobmif.core.theme.SheetCourt
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy
import com.Faprire4.Kobmif.core.theme.VelvetHighlight
import com.Faprire4.Kobmif.core.ui.EmptyStateMessage
import com.Faprire4.Kobmif.core.ui.FavourMeter
import com.Faprire4.Kobmif.core.ui.MaskBadge
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.SecondaryButton
import com.Faprire4.Kobmif.core.ui.TestimonyNote
import com.Faprire4.Kobmif.core.ui.TieEdgeUi
import com.Faprire4.Kobmif.core.ui.TieNodeUi
import com.Faprire4.Kobmif.core.ui.TieState
import com.Faprire4.Kobmif.core.ui.TiesGraph
import com.Faprire4.Kobmif.domain.model.CaseResult
import com.Faprire4.Kobmif.domain.model.Mark
import com.Faprire4.Kobmif.domain.model.Statement
import com.Faprire4.Kobmif.presentation.dialog.ContradictionDialog
import com.Faprire4.Kobmif.presentation.dialog.CouncilVoteDialog
import com.Faprire4.Kobmif.presentation.dialog.PauseDialog
import com.Faprire4.Kobmif.presentation.dialog.VoteOption

@Composable
fun GameScreen(
    chapterIndex: Int,
    attempt: Int,
    onGameOver: (CaseResult) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(chapterIndex, attempt) {
        gameViewModel.startCase(chapterIndex, attempt)
    }
    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onGameOver(event.result)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    Box(modifier = modifier.fillMaxSize().background(InkBlack)) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(ScrimSoft, ScrimMid, ScrimSoft)))
        )

        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AntiqueGold)
            }
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                titleRes = state.chapterTitleRes,
                questionsLeft = state.questionsLeft,
                questionBudget = state.questionBudget,
                onPause = { gameViewModel.onPause() },
                onHome = { gameViewModel.onPause() }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(118.dp)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.claimants.forEach { claimant ->
                    val mark = state.marks[claimant.id] ?: Mark.NONE
                    MaskBadge(
                        name = stringResource(claimant.nameRes),
                        maskDescription = stringResource(
                            R.string.cd_mask_fmt,
                            stringResource(claimant.nameRes)
                        ),
                        mark = mark,
                        markLabel = markLabel(mark),
                        unmasked = state.unmasked.contains(claimant.id),
                        motive = if (state.unmasked.contains(claimant.id)) {
                            stringResource(R.string.motive_fmt, stringResource(claimant.motiveRes))
                        } else {
                            null
                        },
                        selected = state.selectedClaimantId == claimant.id,
                        steady = state.steady,
                        onClick = { gameViewModel.onSelectClaimant(claimant.id) }
                    )
                }
            }

            PaneSwitch(
                pane = state.pane,
                onSwitch = { gameViewModel.onSwitchPane(it) }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (state.pane == BoardPane.EVIDENCE) {
                    EvidencePane(
                        notes = state.boardNotes,
                        claimantNames = state.claimants.associate { it.id to it.nameRes },
                        selectedIds = state.selectedNoteIds,
                        stampedIds = state.stampedNoteIds,
                        hintedIds = state.hintedNoteIds,
                        steady = state.steady,
                        onToggle = { gameViewModel.onToggleNote(it) }
                    )
                } else {
                    TiesPane(state = state)
                }

                if (state.missVisible) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SealCrimson.copy(alpha = 0.92f))
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.no_contradiction),
                            style = MaterialTheme.typography.labelSmall,
                            color = InkBlack
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.game_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            StatusStrip(state = state)

            ActionBlock(
                state = state,
                onQuestion = { gameViewModel.onQuestion() },
                onUnmask = { gameViewModel.onUnmask() },
                onCrossExamine = { gameViewModel.onCrossExamine() },
                onNameHeir = { gameViewModel.onOpenVote() }
            )
        }

        when (val dialog = state.dialog) {
            is GameDialog.Pause -> PauseDialog(
                onResume = { gameViewModel.onResume() },
                onMenu = onMenu
            )

            is GameDialog.Vote -> CouncilVoteDialog(
                options = state.claimants.map { claimant ->
                    VoteOption(
                        id = claimant.id,
                        name = stringResource(claimant.nameRes),
                        markLabel = markLabel(state.marks[claimant.id] ?: Mark.NONE),
                        motive = if (state.unmasked.contains(claimant.id)) {
                            stringResource(claimant.motiveRes)
                        } else {
                            null
                        }
                    )
                },
                selectedId = dialog.selectedId,
                onSelect = { gameViewModel.onVoteSelect(it) },
                onConfirm = { gameViewModel.onConfirmVote() },
                onCancel = { gameViewModel.onCancelVote() }
            )

            is GameDialog.Contradiction -> ContradictionDialog(
                firstLine = noteText(state.revealed, dialog.firstId),
                secondLine = noteText(state.revealed, dialog.secondId),
                sealRotation = sealRotation(dialog.firstId),
                steady = state.steady,
                onContinue = { gameViewModel.onDismissContradiction() }
            )

            is GameDialog.None -> Spacer(modifier = Modifier.size(0.dp))
        }
    }
}

@Composable
private fun GameHeader(
    titleRes: Int,
    questionsLeft: Int,
    questionBudget: Int,
    onPause: () -> Unit,
    onHome: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(HeaderScrim)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .semantics { liveRegion = LiveRegionMode.Polite },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.questions_fmt, questionsLeft, questionBudget),
                style = MaterialTheme.typography.titleMedium,
                color = Candlelight,
                maxLines = 1
            )
        }
        IconButton(onClick = onHome, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_home),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(GoldBorder)
    )
}

@Composable
private fun PaneSwitch(
    pane: BoardPane,
    onSwitch: (BoardPane) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PaneTab(
            label = stringResource(R.string.pane_evidence),
            selected = pane == BoardPane.EVIDENCE,
            onClick = { onSwitch(BoardPane.EVIDENCE) },
            modifier = Modifier.weight(1f)
        )
        PaneTab(
            label = stringResource(R.string.pane_ties),
            selected = pane == BoardPane.TIES,
            onClick = { onSwitch(BoardPane.TIES) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PaneTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .height(40.dp)
            .background(if (selected) VelvetBurgundy else SurfaceRaised, shape)
            .border(1.dp, if (selected) GoldBorder else InertBorder, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Candlelight else TextSecondary,
            maxLines = 1
        )
    }
}

@Composable
private fun EvidencePane(
    notes: List<Statement>,
    claimantNames: Map<Int, Int>,
    selectedIds: List<Int>,
    stampedIds: Set<Int>,
    hintedIds: Set<Int>,
    steady: Boolean,
    onToggle: (Int) -> Unit
) {
    if (notes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyStateMessage(
                text = stringResource(R.string.board_empty),
                spriteRes = R.drawable.sprite_5
            )
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(notes, key = { it.id }) { note ->
            val speakerRes = claimantNames[note.speakerId]
            TestimonyNote(
                speaker = if (speakerRes != null) stringResource(speakerRes) else "",
                body = if (note.targetNameRes != 0) {
                    stringResource(note.textRes, stringResource(note.targetNameRes))
                } else {
                    stringResource(note.textRes)
                },
                selected = selectedIds.contains(note.id),
                stamped = stampedIds.contains(note.id),
                hinted = hintedIds.contains(note.id),
                sealRotation = sealRotation(note.id),
                steady = steady,
                onClick = { onToggle(note.id) }
            )
        }
    }
}

@Composable
private fun TiesPane(state: GameUiState) {
    val nodes = state.claimants.map { claimant ->
        val name = stringResource(claimant.nameRes)
        TieNodeUi(
            initial = name.trim().split(" ").last().take(1),
            name = name,
            unmasked = state.unmasked.contains(claimant.id)
        )
    }
    val speakersWithNotes = state.revealed.map { it.speakerId }.toSet()
    val edges = state.ties.map { tie ->
        val confirmed = state.unmasked.contains(tie.fromId) || state.unmasked.contains(tie.toId)
        val suspected = speakersWithNotes.contains(tie.fromId) || speakersWithNotes.contains(tie.toId)
        TieEdgeUi(
            fromIndex = tie.fromId,
            toIndex = tie.toId,
            label = stringResource(tie.labelRes),
            state = when {
                confirmed -> TieState.CONFIRMED
                suspected -> TieState.SUSPECTED
                else -> TieState.HIDDEN
            }
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TiesGraph(
            nodes = nodes,
            edges = edges,
            steady = state.steady,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(10.dp)
        )
        if (edges.none { it.state == TieState.CONFIRMED }) {
            Text(
                text = stringResource(R.string.ties_empty),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun StatusStrip(state: GameUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .background(SurfaceRaised.copy(alpha = 0.9f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
            Text(
                text = stringResource(R.string.contradictions_fmt, state.contradictions),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.questions_fmt, state.questionsLeft, state.questionBudget),
                style = MaterialTheme.typography.labelSmall,
                color = Candlelight,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        FavourMeter(
            label = stringResource(R.string.favour_label),
            value = state.favour,
            max = state.favourMax,
            readout = stringResource(R.string.favour_fmt, state.favour, state.favourMax),
            steady = state.steady
        )
    }
}

@Composable
private fun ActionBlock(
    state: GameUiState,
    onQuestion: () -> Unit,
    onUnmask: () -> Unit,
    onCrossExamine: () -> Unit,
    onNameHeir: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(24.dp, RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
            .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
            .background(SheetCourt)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrimaryButton(
                text = stringResource(R.string.question),
                iconRes = R.drawable.ic_question,
                onClick = onQuestion,
                enabled = state.canQuestion,
                steady = state.steady,
                height = 54.dp,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.unmask),
                onClick = onUnmask,
                enabled = state.canUnmask,
                height = 54.dp,
                modifier = Modifier.weight(1f)
            )
        }
        SecondaryButton(
            text = stringResource(R.string.cross_examine),
            onClick = onCrossExamine,
            enabled = state.canCrossExamine,
            height = 50.dp,
            modifier = Modifier.fillMaxWidth()
        )
        PrimaryButton(
            text = stringResource(R.string.name_the_heir),
            onClick = onNameHeir,
            steady = state.steady,
            height = 54.dp,
            gradient = listOf(SealCrimson, VelvetHighlight),
            contentColor = Candlelight,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun markLabel(mark: Mark): String = when (mark) {
    Mark.NONE -> stringResource(R.string.mark_none)
    Mark.SUSPECT -> stringResource(R.string.mark_suspect)
    Mark.CLEARED -> stringResource(R.string.mark_cleared)
}

@Composable
private fun noteText(notes: List<Statement>, id: Int): String {
    val note = notes.firstOrNull { it.id == id } ?: return ""
    return if (note.targetNameRes != 0) {
        stringResource(note.textRes, stringResource(note.targetNameRes))
    } else {
        stringResource(note.textRes)
    }
}

private fun sealRotation(id: Int): Float = ((id * 37) % 15 - 7).toFloat()
