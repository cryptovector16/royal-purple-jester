package com.Faprire4.Kobmif.core.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.InertBorder
import com.Faprire4.Kobmif.core.theme.SealCrimson
import com.Faprire4.Kobmif.core.theme.SurfaceRaised
import com.Faprire4.Kobmif.core.theme.TextMuted
import com.Faprire4.Kobmif.core.theme.TextSecondary
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy
import com.Faprire4.Kobmif.domain.model.Mark

@Composable
fun MaskBadge(
    name: String,
    maskDescription: String,
    mark: Mark,
    markLabel: String,
    unmasked: Boolean,
    motive: String?,
    selected: Boolean,
    steady: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ringColor = when (mark) {
        Mark.NONE -> InertBorder
        Mark.SUSPECT -> SealCrimson
        Mark.CLEARED -> AntiqueGold
    }
    val ringWidth by animateDpAsState(
        targetValue = if (mark == Mark.NONE) 1.dp else 2.dp,
        animationSpec = tween(if (steady) 0 else 240),
        label = "maskRing"
    )
    Column(
        modifier = modifier
            .width(84.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
            .semantics { stateDescription = markLabel },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .background(
                    Brush.verticalGradient(listOf(SurfaceRaised, VelvetBurgundy)),
                    CircleShape
                )
                .border(ringWidth, ringColor, CircleShape)
                .border(
                    width = if (selected) 2.dp else 0.dp,
                    color = if (selected) Candlelight else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = maskDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(44.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Candlelight else TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (unmasked && motive != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = motive,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .background(SealCrimson.copy(alpha = 0.22f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}
