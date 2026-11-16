package com.Faprire4.Kobmif.presentation.caseselect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.di.ServiceLocator
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.InertBorder
import com.Faprire4.Kobmif.core.theme.InkBlack
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy
import com.Faprire4.Kobmif.core.ui.EngravedCard
import com.Faprire4.Kobmif.core.ui.SecondaryButton
import com.Faprire4.Kobmif.core.ui.SectionHeader
import com.Faprire4.Kobmif.domain.model.Chapter
import com.Faprire4.Kobmif.domain.model.Ending

@Composable
fun CaseSelectScreen(
    onOpenChapter: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val caseSelectViewModel: CaseSelectViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by caseSelectViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        caseSelectViewModel.refresh()
    }

    Box(modifier = modifier.fillMaxSize().background(InkBlack)) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.12f)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(InkBlack.copy(alpha = 0.9f), VelvetBurgundy.copy(alpha = 0.85f), InkBlack)
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            SectionHeader(
                title = stringResource(R.string.case_select_title),
                leadingRes = R.drawable.ic_back,
                leadingDescription = stringResource(R.string.cd_back),
                onLeading = onMenu,
                trailing = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_seal),
                            contentDescription = null,
                            tint = AntiqueGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.seals_fmt, state.seals, state.totalChapters),
                            style = MaterialTheme.typography.labelSmall,
                            color = Candlelight,
                            maxLines = 1
                        )
                    }
                }
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 14.dp,
                    bottom = 14.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(state.chapters, key = { it.index }) { chapter ->
                    ChapterRow(chapter = chapter, onOpen = { onOpenChapter(chapter.index) })
                }
            }

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun ChapterRow(
    chapter: Chapter,
    onOpen: () -> Unit
) {
    val lockedLabel = stringResource(R.string.locked)
    val accent = if (chapter.unlocked) AntiqueGold else InertBorder
    EngravedCard(
        accent = accent,
        contentPadding = 14.dp,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (chapter.unlocked) 1f else 0.45f)
            .then(if (chapter.unlocked) Modifier.clickable(onClick = onOpen) else Modifier)
            .semantics {
                if (!chapter.unlocked) {
                    stateDescription = lockedLabel
                }
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(VelvetBurgundy, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.chapter_numeral_fmt, chapter.index + 1),
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (chapter.unlocked) Candlelight else TextMuted
                )
                if (chapter.solved) {
                    Image(
                        painter = painterResource(R.drawable.sprite_3),
                        contentDescription = stringResource(R.string.cd_seal),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(chapter.titleRes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (chapter.unlocked) TextPrimary else TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.chapter_meta_fmt,
                        4,
                        chapter.statementsPerClaimant
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.ending_fmt, endingLabel(chapter.ending)),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            if (chapter.unlocked) {
                Text(
                    text = stringResource(R.string.open),
                    style = MaterialTheme.typography.titleMedium,
                    color = Candlelight,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .height(48.dp)
                        .clickable(onClick = onOpen)
                        .padding(horizontal = 6.dp, vertical = 12.dp)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = stringResource(R.string.cd_locked),
                    tint = TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun endingLabel(ending: Ending?): String = when (ending) {
    Ending.CROWN_RESTORED -> stringResource(R.string.ending_crown_restored)
    Ending.QUIET_CORONATION -> stringResource(R.string.ending_quiet_coronation)
    Ending.JESTER_LAUGHS -> stringResource(R.string.ending_jester_laughs)
    Ending.COURT_IN_SHADOW -> stringResource(R.string.ending_court_in_shadow)
    null -> stringResource(R.string.ending_none)
}
