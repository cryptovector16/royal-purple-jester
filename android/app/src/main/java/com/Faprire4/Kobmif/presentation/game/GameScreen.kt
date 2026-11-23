package com.Faprire4.Kobmif.presentation.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.Faprire4.Kobmif.core.theme.Crimson
import com.Faprire4.Kobmif.core.theme.InkSilhouette
import com.Faprire4.Kobmif.core.theme.SurfaceCard
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CourtBackdrop
import com.Faprire4.Kobmif.core.ui.CourtLabels
import com.Faprire4.Kobmif.core.ui.HeaderUnderline
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.ScreenHeader
import com.Faprire4.Kobmif.core.ui.SecondaryButton
import com.Faprire4.Kobmif.core.ui.SegmentedTabs
import com.Faprire4.Kobmif.core.ui.StatEntry
import com.Faprire4.Kobmif.core.ui.StatRow
import com.Faprire4.Kobmif.presentation.dialog.AccusationDialog
import com.Faprire4.Kobmif.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    chapterIndex: Int,
    session: Int,
    onVerdict: () -> Unit,
    onMenu: () -> Unit,
    onRules: () -> Unit
) {
    val model: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by model.state.collectAsStateWithLifecycle()

    LaunchedEffect(session) {
        model.openChapter(chapterIndex, session)
    }
    LaunchedEffect(Unit) {
        model.verdictReached.collect { onVerdict() }
    }
    DisposableEffect(Unit) {
        onDispose { model.onScreenLeft() }
    }

    GameContent(
        state = state,
        onMenu = onMenu,
        onPause = model::pause,
        onTab = model::selectTab,
        onSeal = model::cycleSeal,
        onSelect = model::selectCourtier,
        onDeal = model::dealClaim,
        onLock = model::lockPair,
        onUnmask = model::sealOfTruth,
        onAccuse = model::openAccusation
    )

    if (state.accusing) {
        AccusationDialog(
            requireConfirm = state.confirmAccusation,
            maskColours = state.maskColours,
            onName = model::nameHeir,
            onDismiss = model::dismissAccusation
        )
    }
    if (state.paused) {
        PauseDialog(
            onResume = model::resume,
            onRules = onRules,
            onMenu = onMenu
        )
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onMenu: () -> Unit,
    onPause: () -> Unit,
    onTab: (Int) -> Unit,
    onSeal: (Int) -> Unit,
    onSelect: (Int) -> Unit,
    onDeal: () -> Unit,
    onLock: () -> Unit,
    onUnmask: () -> Unit,
    onAccuse: () -> Unit
) {
    CourtBackdrop(
        overlay = listOf(
            Color(0xCC07070A),
            Color(0xA81C0E14),
            Color(0xE007070A)
        ),
        glow = Candlelight.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(
                    R.string.game_chapter_banner,
                    stringResource(CourtLabels.chapterNumeral(state.chapterIndex)),
                    stringResource(CourtLabels.chapterName(state.chapterIndex))
                ),
                onMenu = onMenu,
                trailingIcon = R.drawable.icon_pause,
                trailingDescription = stringResource(R.string.cd_pause),
                onTrailing = onPause
            )
            HeaderUnderline()
            SegmentedTabs(
                options = listOf(
                    stringResource(R.string.game_tab_evidence),
                    stringResource(R.string.game_tab_court)
                ),
                selectedIndex = state.tab,
                onSelect = onTab,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = when {
                    state.voteCalled -> stringResource(R.string.game_hint_vote)
                    state.tab == 0 -> stringResource(R.string.game_hint_evidence)
                    else -> stringResource(R.string.game_hint_court)
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (state.voteCalled) Crimson else TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (state.tab == 0) {
                    EvidencePanel(state = state, onSeal = onSeal)
                } else {
                    CourtPanel(state = state, onSelect = onSelect)
                }
            }
            StatStrip(state = state)
            Controls(
                state = state,
                onDeal = onDeal,
                onLock = onLock,
                onUnmask = onUnmask,
                onAccuse = onAccuse
            )
        }
        if (state.sealing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(InkSilhouette.copy(alpha = 0.78f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.game_sealing),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Candlelight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun StatStrip(state: GameUiState) {
    val star = stringResource(R.string.glyph_star)
    val spent = stringResource(R.string.glyph_dot)
    val patienceText = buildString {
        for (index in 0 until state.patienceTotal) {
            append(if (index < state.patience) star else spent)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatRow(
            entries = listOf(
                StatEntry(
                    label = stringResource(R.string.game_stat_questions),
                    value = stringResource(
                        R.string.game_questions_value,
                        state.questionsLeft,
                        state.questionsTotal
                    ),
                    accent = Candlelight,
                    hideWhenEmpty = false
                ),
                StatEntry(
                    label = stringResource(R.string.game_stat_patience),
                    value = patienceText,
                    accent = Crimson,
                    hideWhenEmpty = false
                ),
                StatEntry(
                    label = stringResource(R.string.game_stat_insight),
                    value = state.insight.toString(),
                    accent = AntiqueGold
                )
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.game_target),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
        )
        Text(
            text = stringResource(R.string.game_masks_known, state.maskedCount),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Controls(
    state: GameUiState,
    onDeal: () -> Unit,
    onLock: () -> Unit,
    onUnmask: () -> Unit,
    onAccuse: () -> Unit
) {
    val shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceCard.copy(alpha = 0.97f), shape)
            .border(1.dp, AntiqueGold.copy(alpha = 0.35f), shape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (state.selected >= 0) {
                stringResource(
                    R.string.game_questioning,
                    stringResource(CourtLabels.nameOf(state.selected))
                )
            } else {
                stringResource(R.string.game_select_none)
            },
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        PrimaryButton(
            text = stringResource(R.string.cta_deal),
            onClick = onDeal,
            enabled = state.canDeal,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SecondaryButton(
                text = stringResource(R.string.cta_lock),
                onClick = onLock,
                enabled = state.canLock,
                accent = Candlelight,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.cta_unmask),
                onClick = onUnmask,
                enabled = state.canUnmask,
                modifier = Modifier.weight(1f)
            )
        }
        SecondaryButton(
            text = stringResource(R.string.cta_accuse),
            onClick = onAccuse,
            enabled = state.canAccuse,
            accent = Crimson,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
        )
    }
}
