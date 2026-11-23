package com.Faprire4.Kobmif.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.Crimson
import com.Faprire4.Kobmif.core.theme.NeutralSeal
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.VelvetDeep
import com.Faprire4.Kobmif.domain.model.SealState

@Composable
fun WaxSeal(
    state: SealState,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = when (state) {
        SealState.UNMARKED -> stringResource(R.string.seal_unmarked)
        SealState.MARKED_TRUE -> stringResource(R.string.seal_true)
        SealState.MARKED_FALSE -> stringResource(R.string.seal_false)
    }
    val glyph = when (state) {
        SealState.UNMARKED -> ""
        SealState.MARKED_TRUE -> stringResource(R.string.glyph_check)
        SealState.MARKED_FALSE -> stringResource(R.string.glyph_cross)
    }
    val flip by animateFloatAsState(
        targetValue = when (state) {
            SealState.UNMARKED -> 0f
            SealState.MARKED_TRUE -> 180f
            SealState.MARKED_FALSE -> 360f
        },
        animationSpec = tween(260),
        label = "sealFlip"
    )
    val fill = when (state) {
        SealState.UNMARKED -> VelvetDeep
        SealState.MARKED_TRUE -> AntiqueGold
        SealState.MARKED_FALSE -> Crimson
    }
    val rim = when (state) {
        SealState.UNMARKED -> NeutralSeal
        SealState.MARKED_TRUE -> Candlelight
        SealState.MARKED_FALSE -> Candlelight.copy(alpha = 0.6f)
    }
    val base = modifier
        .size(48.dp)
        .graphicsLayer { rotationY = flip }
        .background(fill, CircleShape)
        .border(2.dp, rim, CircleShape)
        .semantics { stateDescription = label }
    Box(
        modifier = if (enabled) base.clickable(onClick = onClick) else base,
        contentAlignment = Alignment.Center
    ) {
        if (glyph.isNotEmpty()) {
            Text(
                text = glyph,
                style = MaterialTheme.typography.titleMedium,
                color = if (state == SealState.MARKED_TRUE) VelvetDeep else TextPrimary
            )
        }
    }
}
