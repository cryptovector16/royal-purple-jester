package com.Faprire4.Kobmif.presentation.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.Crimson
import com.Faprire4.Kobmif.core.theme.MaskColours
import com.Faprire4.Kobmif.core.theme.NeutralSeal
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.ui.CourtLabels
import com.Faprire4.Kobmif.core.ui.CourtierMedallion
import com.Faprire4.Kobmif.core.ui.EngravedPlate
import com.Faprire4.Kobmif.domain.model.CourtierRole
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun CourtPanel(
    state: GameUiState,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val side = min(maxWidth.value - 8f, min(maxHeight.value, 320f))
            val nodeSize = 62f
            val orbit = side / 2f - nodeSize / 2f - 6f
            Box(modifier = Modifier.size(side.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centre = Offset(size.width / 2f, size.height / 2f)
                    val scale = size.width / side
                    for (index in 0 until 5) {
                        val next = (index + 1) % 5
                        drawLine(
                            color = NeutralSeal,
                            start = nodeCentre(centre, orbit * scale, index, state.graphRotationDeg),
                            end = nodeCentre(centre, orbit * scale, next, state.graphRotationDeg),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f))
                        )
                    }
                    state.edges.forEach { edge ->
                        drawLine(
                            color = if (edge.kin) AntiqueGold else Crimson,
                            start = nodeCentre(centre, orbit * scale, edge.first, state.graphRotationDeg),
                            end = nodeCentre(centre, orbit * scale, edge.second, state.graphRotationDeg),
                            strokeWidth = 5f,
                            cap = StrokeCap.Round
                        )
                    }
                }
                for (index in 0 until 5) {
                    val angle = angleOf(index, state.graphRotationDeg)
                    val dx = (orbit * cos(angle)).dp
                    val dy = (orbit * sin(angle)).dp
                    val node = state.nodes.getOrNull(index)
                    val rim = when {
                        node?.revealedRole == CourtierRole.TRUTHFUL -> AntiqueGold
                        node?.revealedRole == CourtierRole.MASKED -> Crimson
                        state.selected == index -> Candlelight
                        else -> NeutralSeal
                    }
                    CourtierMedallion(
                        courtierIndex = index,
                        side = nodeSize.dp,
                        rim = rim,
                        tint = if (state.maskColours) MaskColours[index % MaskColours.size] else null,
                        badge = node?.claimedMotive?.let { stringResource(CourtLabels.motiveBadge(it)) },
                        onClick = { onSelect(index) },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = dx, y = dy)
                    )
                }
            }
        }
        Dossier(state = state)
    }
}

@Composable
private fun Dossier(state: GameUiState) {
    val index = state.selected
    EngravedPlate(
        modifier = Modifier.fillMaxWidth(),
        radius = 14.dp,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (index < 0) {
                Text(
                    text = stringResource(R.string.game_select_none),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            } else {
                val node = state.nodes.getOrNull(index)
                Text(
                    text = stringResource(CourtLabels.nameOf(index)),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.mask_label, stringResource(CourtLabels.maskOf(index))),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = stringResource(R.string.spoken_label, node?.claimsSpoken ?: 0),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (node?.revealedRole) {
                        CourtierRole.TRUTHFUL -> stringResource(R.string.dossier_truthful)
                        CourtierRole.MASKED -> stringResource(R.string.dossier_masked)
                        else -> stringResource(R.string.dossier_unknown)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = when (node?.revealedRole) {
                        CourtierRole.TRUTHFUL -> AntiqueGold
                        CourtierRole.MASKED -> Crimson
                        else -> TextMuted
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = node?.claimedMotive?.let {
                        stringResource(R.string.dossier_motive, stringResource(CourtLabels.motiveLabel(it)))
                    } ?: stringResource(R.string.motive_unknown),
                    style = MaterialTheme.typography.labelSmall,
                    color = Candlelight
                )
            }
        }
    }
}

private fun angleOf(index: Int, rotationDeg: Float): Float {
    val degrees = -90f + 72f * index + rotationDeg
    return (degrees * PI / 180f).toFloat()
}

private fun nodeCentre(centre: Offset, orbit: Float, index: Int, rotationDeg: Float): Offset {
    val angle = angleOf(index, rotationDeg)
    return Offset(centre.x + orbit * cos(angle), centre.y + orbit * sin(angle))
}
