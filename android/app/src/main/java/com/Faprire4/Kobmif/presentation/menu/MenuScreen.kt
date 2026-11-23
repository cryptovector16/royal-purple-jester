package com.Faprire4.Kobmif.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.InkSilhouette
import com.Faprire4.Kobmif.core.theme.SurfaceCard
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CornerFlourish
import com.Faprire4.Kobmif.core.ui.CourtBackdrop
import com.Faprire4.Kobmif.core.ui.CourtLabels
import com.Faprire4.Kobmif.core.ui.PrimaryButton
import com.Faprire4.Kobmif.core.ui.SecondaryButton
import com.Faprire4.Kobmif.core.ui.StatEntry
import com.Faprire4.Kobmif.core.ui.StatRow
import com.Faprire4.Kobmif.presentation.dialog.ResetRecordsDialog
import com.Faprire4.Kobmif.presentation.dialog.SettingsDialog

@Composable
fun MenuScreen(
    onPlay: (Int) -> Unit,
    onChapters: () -> Unit,
    onRules: () -> Unit
) {
    val model: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by model.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        model.reload()
    }

    MenuContent(
        state = state,
        onPlay = { onPlay(state.nextChapterIndex) },
        onChapters = onChapters,
        onRules = onRules,
        onSettings = model::openSettings
    )

    if (state.settingsOpen) {
        SettingsDialog(
            settings = state.settings,
            onToggleConfirm = model::toggleConfirmAccusation,
            onToggleMotion = model::toggleCandleMotion,
            onToggleMasks = model::toggleMaskColours,
            onReset = model::askReset,
            onDismiss = model::closeSettings
        )
    }
    if (state.resetOpen) {
        ResetRecordsDialog(
            onConfirm = model::confirmReset,
            onDismiss = model::dismissReset
        )
    }
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onChapters: () -> Unit,
    onRules: () -> Unit,
    onSettings: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val sheetAlpha by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(420, easing = FastOutSlowInEasing),
        label = "sheetAlpha"
    )

    CourtBackdrop(
        overlay = listOf(
            Color(0x9407070A),
            Color(0x571C0E14),
            Color(0xEB1C0E14)
        ),
        glow = Candlelight.copy(alpha = 0.18f)
    ) {
        CornerFlourish(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 60.dp, end = 12.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Tableau(modifier = Modifier.weight(1f))
            Sheet(
                state = state,
                alpha = sheetAlpha,
                onPlay = onPlay,
                onChapters = onChapters,
                onRules = onRules,
                onSettings = onSettings
            )
        }
    }
}

@Composable
private fun Tableau(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .size(132.dp)
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            for (index in 0 until 5) {
                Image(
                    painter = painterResource(CourtLabels.spriteOf(index)),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(InkSilhouette),
                    modifier = Modifier
                        .padding(bottom = if (index % 2 == 0) 0.dp else 12.dp)
                        .size(62.dp)
                        .alpha(0.34f)
                )
            }
        }
    }
}

@Composable
private fun Sheet(
    state: MenuUiState,
    alpha: Float,
    onPlay: () -> Unit,
    onChapters: () -> Unit,
    onRules: () -> Unit,
    onSettings: () -> Unit
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .shadow(18.dp, shape, ambientColor = InkSilhouette, spotColor = AntiqueGold)
            .background(SurfaceCard.copy(alpha = 0.97f), shape)
            .border(1.dp, AntiqueGold.copy(alpha = 0.38f), shape)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 4.dp)
                .background(AntiqueGold.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
        )
        Text(
            text = stringResource(R.string.menu_title),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.menu_tagline),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        if (state.hasRecord) {
            StatRow(
                entries = listOf(
                    StatEntry(
                        label = stringResource(R.string.menu_stat_solved),
                        value = state.casesSolved.toString(),
                        accent = AntiqueGold
                    ),
                    StatEntry(
                        label = stringResource(R.string.menu_stat_open),
                        value = state.chaptersOpen.toString(),
                        accent = Candlelight
                    )
                )
            )
            val best = state.bestEnding
            if (best != null) {
                Text(
                    text = stringResource(
                        R.string.menu_best_verdict,
                        stringResource(CourtLabels.endingTitle(best))
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = Candlelight,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text(
                text = stringResource(R.string.menu_no_record),
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .widthIn(min = 120.dp)
                .height(38.dp)
                .background(SurfaceRaised, RoundedCornerShape(19.dp))
                .border(1.dp, AntiqueGold.copy(alpha = 0.45f), RoundedCornerShape(19.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    R.string.menu_next_case,
                    stringResource(CourtLabels.chapterNumeral(state.nextChapterIndex)),
                    stringResource(CourtLabels.chapterName(state.nextChapterIndex))
                ),
                style = MaterialTheme.typography.labelSmall,
                color = Candlelight,
                maxLines = 1
            )
        }

        Text(
            text = stringResource(R.string.menu_hint),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(2.dp))
        PrimaryButton(
            text = stringResource(R.string.cta_play),
            onClick = onPlay,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = stringResource(R.string.cta_chapters),
                onClick = onChapters,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.cta_rules),
                onClick = onRules,
                modifier = Modifier.weight(1f)
            )
        }
        SecondaryButton(
            text = stringResource(R.string.cta_settings),
            onClick = onSettings,
            accent = TextSecondary,
            fill = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.disclaimer),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}
