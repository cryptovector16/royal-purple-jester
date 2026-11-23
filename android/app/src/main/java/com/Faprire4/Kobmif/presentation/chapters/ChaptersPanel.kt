package com.Faprire4.Kobmif.presentation.chapters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.Crimson
import com.Faprire4.Kobmif.core.theme.NeutralSeal
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CourtBackdrop
import com.Faprire4.Kobmif.core.ui.CourtLabels
import com.Faprire4.Kobmif.core.ui.EngravedPlate
import com.Faprire4.Kobmif.core.ui.HeaderUnderline
import com.Faprire4.Kobmif.core.ui.ScreenHeader
import com.Faprire4.Kobmif.core.ui.SecondaryButton

@Composable
fun ChapterSelectScreen(
    onOpenChapter: (Int) -> Unit,
    onMenu: () -> Unit
) {
    val model: ChaptersViewModel = viewModel(factory = ServiceLocator.factory)
    val state by model.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        model.reload()
    }

    CourtBackdrop(
        overlay = listOf(
            Color(0xE007070A),
            Color(0xCC1C0E14),
            Color(0xE607070A)
        ),
        glow = AntiqueGold.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.chapters_title),
                onMenu = onMenu
            )
            HeaderUnderline()
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val contentWidth = if (maxWidth - 32.dp < 380.dp) maxWidth - 32.dp else 380.dp
                val gap = 12.dp
                val tile = (contentWidth - gap) / 2
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(contentWidth)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(gap)
                ) {
                    for (rowStart in state.tiles.indices step 2) {
                        Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                            for (offset in 0 until 2) {
                                val tileState = state.tiles.getOrNull(rowStart + offset)
                                if (tileState != null) {
                                    ChapterTile(
                                        tileState = tileState,
                                        side = tile,
                                        onClick = { onOpenChapter(tileState.index) }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.width(tile))
                                }
                            }
                        }
                    }
                    if (state.allSealed) {
                        Text(
                            text = stringResource(R.string.chapters_hint),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        )
                    }
                }
            }
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun ChapterTile(
    tileState: ChapterTileState,
    side: Dp,
    onClick: () -> Unit
) {
    val sealedLabel = stringResource(R.string.state_sealed)
    val openLabel = stringResource(R.string.state_open)
    val solved = tileState.bestEnding?.victory == true
    val dot = when {
        solved -> AntiqueGold
        tileState.bestEnding != null -> Crimson
        else -> NeutralSeal
    }
    val base = Modifier
        .width(side)
        .height(side * 0.86f)
        .semantics { stateDescription = if (tileState.unlocked) openLabel else sealedLabel }
    EngravedPlate(
        modifier = if (tileState.unlocked) base.clickable(onClick = onClick) else base.alpha(0.45f),
        radius = 18.dp,
        accent = if (tileState.unlocked) AntiqueGold else NeutralSeal,
        borderAlpha = 0.4f,
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(dot, CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(CourtLabels.chapterNumeral(tileState.index)),
                style = MaterialTheme.typography.displaySmall,
                color = if (tileState.unlocked) AntiqueGold else NeutralSeal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(CourtLabels.chapterName(tileState.index)),
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tileState.bestEnding?.let { stringResource(CourtLabels.endingTitle(it)) }
                    ?: stringResource(R.string.chapter_masks, tileState.maskedCount),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
