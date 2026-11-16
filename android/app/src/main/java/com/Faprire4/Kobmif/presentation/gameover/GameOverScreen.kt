package com.Faprire4.Kobmif.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.Faprire4.Kobmif.core.config.GameConfig
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.InkBlack
import com.Faprire4.Kobmif.core.theme.SealCrimson
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.theme.VelvetHighlight
import com.Faprire4.Kobmif.core.ui.EngravedCard
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.SecondaryButton
import com.Faprire4.Kobmif.core.ui.StatCard
import com.Faprire4.Kobmif.domain.model.CaseResult
import com.Faprire4.Kobmif.domain.model.Ending

@Composable
fun GameOverScreen(
    result: CaseResult,
    onPlayAgain: () -> Unit,
    onNextCase: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(result) {
        entered = true
        gameOverViewModel.refresh()
    }
    val headlineScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.9f,
        animationSpec = tween(520),
        label = "verdictScale"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(520),
        label = "verdictAlpha"
    )

    val haloColor = if (result.correct) Candlelight else SealCrimson
    val headlineColor = when (result.ending) {
        Ending.CROWN_RESTORED -> Candlelight
        Ending.QUIET_CORONATION -> AntiqueGold
        Ending.JESTER_LAUGHS -> SealCrimson
        Ending.COURT_IN_SHADOW -> TextMuted
    }
    val hasNextCase = result.correct && result.chapterIndex + 1 < GameConfig.CHAPTERS

    Box(modifier = modifier.fillMaxSize().background(InkBlack)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(InkBlack, VelvetHighlight, InkBlack))
                )
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(haloColor.copy(alpha = 0.22f), Color.Transparent),
                    center = Offset(size.width / 2f, size.height * 0.3f),
                    radius = 560f
                ),
                radius = 560f,
                center = Offset(size.width / 2f, size.height * 0.3f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(result.chapterTitleRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = endingTitle(result.ending),
                style = MaterialTheme.typography.displaySmall,
                color = headlineColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .scale(headlineScale)
                    .alpha(contentAlpha)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                EngravedCard(
                    accent = headlineColor,
                    contentPadding = 20.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 18.dp, top = 18.dp)
                        .alpha(contentAlpha)
                ) {
                    Text(
                        text = endingEpilogue(result.ending),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                }
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_seal),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(52.dp)
                        .rotate(-7f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = stringResource(
                        R.string.cd_mask_fmt,
                        stringResource(result.heirNameRes)
                    ),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.truth_fmt, stringResource(result.heirNameRes)),
                    style = MaterialTheme.typography.titleMedium,
                    color = Candlelight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.accused_fmt, stringResource(result.accusedNameRes)),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                result.exposedMotives.forEach { motiveRes ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.motive_fmt, stringResource(motiveRes)),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            StatRow(result = result)

            Spacer(modifier = Modifier.height(22.dp))

            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                height = 58.dp,
                modifier = Modifier.fillMaxWidth()
            )
            if (hasNextCase) {
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(
                    text = stringResource(R.string.next_case),
                    onClick = { onNextCase(result.chapterIndex + 1) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.seals_fmt, state.seals, state.totalChapters),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatRow(result: CaseResult) {
    val cards = mutableListOf<Triple<String, String, Color>>()
    if (result.insight > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_insight),
                result.insight.toString(),
                Candlelight
            )
        )
    }
    if (result.contradictions > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_contradictions),
                result.contradictions.toString(),
                SealCrimson
            )
        )
    }
    if (result.questionsLeft > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_questions_left),
                result.questionsLeft.toString(),
                AntiqueGold
            )
        )
    }
    if (cards.size < 2) {
        return
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cards.forEach { card ->
            StatCard(
                label = card.first,
                value = card.second,
                accent = card.third,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun endingTitle(ending: Ending): String = when (ending) {
    Ending.CROWN_RESTORED -> stringResource(R.string.ending_crown_restored)
    Ending.QUIET_CORONATION -> stringResource(R.string.ending_quiet_coronation)
    Ending.JESTER_LAUGHS -> stringResource(R.string.ending_jester_laughs)
    Ending.COURT_IN_SHADOW -> stringResource(R.string.ending_court_in_shadow)
}

@Composable
private fun endingEpilogue(ending: Ending): String = when (ending) {
    Ending.CROWN_RESTORED -> stringResource(R.string.epilogue_crown_restored)
    Ending.QUIET_CORONATION -> stringResource(R.string.epilogue_quiet_coronation)
    Ending.JESTER_LAUGHS -> stringResource(R.string.epilogue_jester_laughs)
    Ending.COURT_IN_SHADOW -> stringResource(R.string.epilogue_court_in_shadow)
}
