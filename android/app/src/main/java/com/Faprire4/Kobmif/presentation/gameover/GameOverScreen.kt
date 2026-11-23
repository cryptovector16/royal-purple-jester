package com.Faprire4.Kobmif.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CornerFlourish
import com.Faprire4.Kobmif.core.ui.CourtBackdrop
import com.Faprire4.Kobmif.core.ui.CourtLabels
import com.Faprire4.Kobmif.core.ui.CourtierMedallion
import com.Faprire4.Kobmif.core.ui.EngravedPlate
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.SecondaryButton
import com.Faprire4.Kobmif.core.ui.StatEntry
import com.Faprire4.Kobmif.core.ui.StatRow
import com.Faprire4.Kobmif.domain.model.Verdict

@Composable
fun GameOverScreen(
    onPlayAgain: (Int) -> Unit,
    onNextChapter: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val model: GameOverViewModel = viewModel(factory = ServiceLocator.factory)
    val state by model.state.collectAsStateWithLifecycle()
    val verdict = state.verdict

    if (verdict == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(InkSilhouette),
            contentAlignment = Alignment.Center
        ) {
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onMenu,
                modifier = Modifier.widthIn(min = 180.dp)
            )
        }
        return
    }

    VerdictContent(
        verdict = verdict,
        nextChapterIndex = state.nextChapterIndex,
        onPlayAgain = { onPlayAgain(verdict.chapterIndex) },
        onNextChapter = { onNextChapter(state.nextChapterIndex) },
        onMenu = onMenu
    )
}

@Composable
private fun VerdictContent(
    verdict: Verdict,
    nextChapterIndex: Int,
    onPlayAgain: () -> Unit,
    onNextChapter: () -> Unit,
    onMenu: () -> Unit
) {
    val victory = verdict.ending.victory
    val accent = if (victory) AntiqueGold else Crimson
    var stamped by remember { mutableStateOf(false) }
    LaunchedEffect(verdict.seed) {
        stamped = true
    }
    val crest by animateFloatAsState(
        targetValue = if (stamped) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 260f),
        label = "crest"
    )

    CourtBackdrop(
        overlay = if (victory) {
            listOf(Color(0xC71C0E14), Color(0xB32A1A12), Color(0xEB07070A))
        } else {
            listOf(Color(0xE607070A), Color(0xDC1A0810), Color(0xF007070A))
        },
        glow = accent.copy(alpha = 0.14f)
    ) {
        CornerFlourish(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 58.dp, start = 12.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.scale(crest)) {
                Box(
                    modifier = Modifier
                        .size(132.dp)
                        .border(1.dp, accent.copy(alpha = 0.32f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(116.dp)
                        .border(2.dp, accent.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CourtierMedallion(
                        courtierIndex = verdict.heirIndex,
                        side = 96.dp,
                        rim = accent
                    )
                }
            }
            Text(
                text = stringResource(CourtLabels.endingTitle(verdict.ending)),
                style = MaterialTheme.typography.displaySmall,
                color = accent,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(
                    if (victory) R.string.verdict_win else R.string.verdict_lose
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(
                    R.string.verdict_reveal,
                    stringResource(CourtLabels.nameOf(verdict.heirIndex))
                ),
                style = MaterialTheme.typography.titleSmall,
                color = Candlelight,
                textAlign = TextAlign.Center
            )
            EngravedPlate(modifier = Modifier.fillMaxWidth(), radius = 14.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(CourtLabels.endingStory(verdict.ending)),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.verdict_seal, verdict.seed),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
            val entries = listOf(
                StatEntry(
                    label = stringResource(R.string.gameover_stat_locks),
                    value = verdict.locks.toString(),
                    accent = Candlelight
                ),
                StatEntry(
                    label = stringResource(R.string.gameover_stat_questions),
                    value = verdict.questionsLeft.toString(),
                    accent = AntiqueGold
                ),
                StatEntry(
                    label = stringResource(R.string.gameover_stat_confidence),
                    value = verdict.confidence.toString(),
                    accent = if (victory) AntiqueGold else Crimson
                )
            )
            if (entries.count { it.visible } >= 2) {
                StatRow(entries = entries)
            } else {
                Text(
                    text = stringResource(R.string.gameover_no_record),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            PrimaryButton(
                text = stringResource(R.string.cta_play_again),
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (nextChapterIndex >= 0) {
                    SecondaryButton(
                        text = stringResource(R.string.cta_next_chapter),
                        onClick = onNextChapter,
                        accent = Candlelight,
                        modifier = Modifier.weight(1f)
                    )
                }
                SecondaryButton(
                    text = stringResource(R.string.cta_menu),
                    onClick = onMenu,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
