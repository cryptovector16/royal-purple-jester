package com.Faprire4.Kobmif.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.Candlelight
import com.Faprire4.Kobmif.core.theme.GoldBorder
import com.Faprire4.Kobmif.core.theme.NoteSurface
import com.Faprire4.Kobmif.core.theme.SealCrimson
import com.Faprire4.Kobmif.core.theme.TextPrimary
import com.Faprire4.Kobmif.core.theme.VelvetBurgundy
import com.Faprire4.Kobmif.core.theme.VelvetHighlight

@Composable
fun TestimonyNote(
    speaker: String,
    body: String,
    selected: Boolean,
    stamped: Boolean,
    hinted: Boolean,
    sealRotation: Float,
    steady: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    val borderColor = when {
        stamped -> SealCrimson
        selected -> Candlelight
        hinted -> Candlelight.copy(alpha = 0.7f)
        else -> GoldBorder
    }
    val borderWidth = if (selected || stamped) 1.6.dp else 1.dp
    val raise by animateFloatAsState(
        targetValue = if (selected && !steady) 1.02f else 1f,
        animationSpec = tween(if (steady) 0 else 220),
        label = "noteRaise"
    )
    val selectedLabel = stringResource(R.string.state_selected)
    val stampedLabel = stringResource(R.string.contradiction_sealed)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(if (selected) 14.dp else 4.dp, shape, ambientColor = Candlelight, spotColor = Color.Black)
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(NoteSurface, VelvetBurgundy)))
            .border(borderWidth, borderColor, shape)
            .clickable(onClick = onClick)
            .semantics {
                if (stamped) {
                    stateDescription = stampedLabel
                } else if (selected) {
                    stateDescription = selectedLabel
                }
            }
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .heightIn(min = 60.dp)
                .height(if (raise > 1f) 92.dp else 86.dp)
                .background(VelvetHighlight)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 12.dp, end = 14.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = speaker,
                    style = MaterialTheme.typography.labelSmall,
                    color = Candlelight
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            if (stamped) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_seal),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(sealRotation)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(22.dp)
                        .alpha(if (hinted) 0.9f else 0.55f)
                )
            }
        }
    }
}
