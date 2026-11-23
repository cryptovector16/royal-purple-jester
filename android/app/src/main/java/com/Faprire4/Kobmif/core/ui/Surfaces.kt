package com.Faprire4.Kobmif.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Faprire4.Kobmif.R
import com.Faprire4.Kobmif.core.theme.AntiqueGold
import com.Faprire4.Kobmif.core.theme.BurgundyAccent
import com.Faprire4.Kobmif.core.theme.InkSilhouette
import com.Faprire4.Kobmif.core.theme.SurfaceCard
import com.Faprire4.Kobmif.core.theme.TextMuted

@Composable
fun CourtBackdrop(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    glow: Color = BurgundyAccent.copy(alpha = 0.22f),
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(glow, Color.Transparent),
                        radius = 820f
                    )
                )
        )
        content()
    }
}

@Composable
fun EngravedPlate(
    modifier: Modifier = Modifier,
    radius: Dp = 16.dp,
    fill: Color = SurfaceCard.copy(alpha = 0.94f),
    accent: Color = AntiqueGold,
    borderAlpha: Float = 0.35f,
    elevation: Dp = 6.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    Box(
        modifier = modifier
            .shadow(elevation, shape, ambientColor = InkSilhouette, spotColor = accent)
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(fill, fill.copy(alpha = (fill.alpha * 0.8f).coerceIn(0f, 1f)))
                )
            )
            .border(1.dp, accent.copy(alpha = borderAlpha), shape),
        content = content
    )
}

@Composable
fun CornerFlourish(modifier: Modifier = Modifier, side: Dp = 56.dp) {
    Image(
        painter = painterResource(R.drawable.ic_corner_flourish),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(side)
            .alpha(0.16f)
    )
}

@Composable
fun EmptyNote(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_wax_seal),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .size(60.dp)
                .alpha(0.3f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GoldDivider(modifier: Modifier = Modifier, width: Dp = 56.dp) {
    Box(
        modifier = modifier
            .size(width = width, height = 1.dp)
            .background(AntiqueGold.copy(alpha = 0.55f))
    )
}
