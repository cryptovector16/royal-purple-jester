package com.Faprire4.Kobmif.presentation.game

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.MaskColours
import com.Faprire4.Kobmif.core.theme.NeutralSeal
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CourtLabels
import com.Faprire4.Kobmif.core.ui.CourtierMedallion
import com.Faprire4.Kobmif.core.ui.EmptyNote
import com.Faprire4.Kobmif.core.ui.EngravedPlate
import com.Faprire4.Kobmif.core.ui.WaxSeal
import com.Faprire4.Kobmif.core.ui.claimSentence

@Composable
fun EvidencePanel(
    state: GameUiState,
    onSeal: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val revealed = state.revealedClaims
    if (revealed.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyNote(text = stringResource(R.string.game_empty_board))
        }
        return
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = revealed, key = { it.id }) { row ->
            ClaimPlate(
                row = row,
                tinted = state.maskColours,
                enabled = !state.blocked,
                onSeal = { onSeal(row.id) }
            )
        }
        item {
            val sealedLeft = state.claims.count { !it.revealed }
            if (sealedLeft > 0) {
                Text(
                    text = stringResource(R.string.claim_sealed_count, sealedLeft),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ClaimPlate(
    row: ClaimRow,
    tinted: Boolean,
    enabled: Boolean,
    onSeal: () -> Unit
) {
    val accent = if (row.locked) Candlelight else AntiqueGold
    val maskTint = if (tinted) MaskColours[row.speaker % MaskColours.size] else null
    EngravedPlate(
        modifier = Modifier
            .widthIn(max = 380.dp)
            .fillMaxWidth(),
        radius = 14.dp,
        accent = accent,
        borderAlpha = if (row.locked) 0.9f else 0.35f,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 76.dp)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CourtierMedallion(
                courtierIndex = row.speaker,
                side = 44.dp,
                rim = maskTint ?: NeutralSeal,
                tint = maskTint
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(CourtLabels.nameOf(row.speaker)),
                    style = MaterialTheme.typography.labelSmall,
                    color = maskTint ?: TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = claimSentence(row.predicate),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                if (!row.core) {
                    Text(
                        text = stringResource(R.string.claim_tag_court),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (row.locked) {
                Box(
                    modifier = Modifier.width(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.glyph_check),
                        style = MaterialTheme.typography.titleLarge,
                        color = Candlelight
                    )
                }
            } else {
                WaxSeal(
                    state = row.seal,
                    enabled = enabled,
                    onClick = onSeal
                )
            }
        }
    }
}
