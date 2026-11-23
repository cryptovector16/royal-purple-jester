package com.Faprire4.Kobmif.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.InkSilhouette
import com.Faprire4.Kobmif.core.theme.SurfaceCard
import com.Faprire4.Kobmif.core.theme.VelvetDeep

@Composable
fun CourtierMedallion(
    courtierIndex: Int,
    side: Dp,
    rim: Color,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    badge: String? = null,
    onClick: (() -> Unit)? = null
) {
    val name = stringResource(CourtLabels.nameOf(courtierIndex))
    val base = modifier
        .size(side)
        .clip(CircleShape)
        .background(
            Brush.radialGradient(listOf(SurfaceCard, VelvetDeep))
        )
        .border(2.dp, rim, CircleShape)
    Box(
        modifier = if (onClick != null) base.clickable(onClick = onClick) else base,
        contentAlignment = Alignment.Center
    ) {
        if (tint != null) {
            Box(
                modifier = Modifier
                    .size(side)
                    .background(tint.copy(alpha = 0.22f), CircleShape)
            )
        }
        Image(
            painter = painterResource(CourtLabels.spriteOf(courtierIndex)),
            contentDescription = stringResource(R.string.cd_courtier, name),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(side * 0.12f)
                .size(side * 0.76f)
        )
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(side * 0.34f)
                    .background(Candlelight, CircleShape)
                    .border(1.dp, AntiqueGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = InkSilhouette,
                    maxLines = 1
                )
            }
        }
    }
}
